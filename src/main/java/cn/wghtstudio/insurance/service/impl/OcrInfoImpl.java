package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.*;
import cn.wghtstudio.insurance.dao.repository.*;
import cn.wghtstudio.insurance.exception.FileTypeException;
import cn.wghtstudio.insurance.exception.OCRException;
import cn.wghtstudio.insurance.service.OcrInfoService;
import cn.wghtstudio.insurance.service.entity.*;
import cn.wghtstudio.insurance.util.AliyunUtil;
import cn.wghtstudio.insurance.util.ocr.*;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class PolicyDealImpl implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PolicyDealImpl.class);
    
    private final String filename;
    
    private final byte[] file;
    
    private final PolicyRepository policyRepository;
    
    private final DrivingLicenseRepository drivingLicenseRepository;
    
    private final CertificateRepository certificateRepository;
    
    private Integer policyId;
    
    private String number = null, plateNumber = null, frame = null, engine = null;
    
    @Nullable
    private String getInsuranceNumber(String words) {
        Pattern pattern = Pattern.compile("保险单号\\s?[:：]\\s?([A-Z]{4}[0-9]{18})");
        Matcher matcher = pattern.matcher(words);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    @Nullable
    private String getPlateNumber(String words) {
        Pattern pattern = Pattern.compile("车牌号\\s?[:：]\\s?([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼](([A-HJ-Z][A-HJ-NP-Z0-9]{5})|([A-HJ-Z](([DF][A-HJ-NP-Z0-9][0-9]{4})|([0-9]{5}[DF])))|([A-HJ-Z][A-D0-9][0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•][0-9]{4}[TDSHBXJ0-9])|([VKHBSLJNGCE][A-DJ-PR-TVY][0-9]{5})");
        Matcher matcher = pattern.matcher(words);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    @Nullable
    private String getFrameNumber(String words) {
        Pattern pattern = Pattern.compile("车架号\\s?[:：]\\s?([A-Z]{3}[A-z0-9]{14})");
        Matcher matcher = pattern.matcher(words);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    @Nullable
    private String getEngineNumber(String words) {
        Pattern pattern = Pattern.compile("发动机号\\s?[:：]\\s?([A-Z0-9]{6,11})");
        Matcher matcher = pattern.matcher(words);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    private void createItemInDB() {
        Policy policy = Policy.builder().
                url("https://versicherung.oss-cn-beijing.aliyuncs.com/" + filename).
                build();
        policyRepository.createPolicy(policy);
        policyId = policy.getId();
    }
    
    private void putPolicyToOSS() {
        AliyunUtil.putObject(filename, new ByteArrayInputStream(file));
    }
    
    private void doOCR() throws IOException, OCRException {
        String b64encoded = Base64.getEncoder().encodeToString(file);
        final String token = GetOcrToken.getAuthToken();
        
        final InsurancePolicyResponse response = OcrInfoGetter.vehicleInsurance(b64encoded, token);
        final List<InsurancePolicyResponse.WordsResult> wordsResultList = response.getWordsResult();
        
        // 从 OCR 结果中提取保险单号 车牌号 车架号 发动机号
        for (InsurancePolicyResponse.WordsResult wordsResult : wordsResultList) {
            String words = wordsResult.getWords();
            if (number == null) {
                number = getInsuranceNumber(words);
            }
            
            if (plateNumber == null) {
                plateNumber = getPlateNumber(words);
            }
            
            if (frame == null) {
                frame = getFrameNumber(words);
            }
            
            if (engine == null) {
                engine = getEngineNumber(words);
            }
        }
        
        if (policyId != null) {
            Policy policy = Policy.builder().
                    id(policyId).
                    number(number).
                    build();
            policyRepository.updatePolicy(policy);
        }
    }
    
    private void matchOrder() {
        boolean isMatch = false;
        List<DrivingLicense> res = drivingLicenseRepository.getDrivingLicenseByPolicyInfo(DrivingLicense.builder().
                engine(engine).frame(frame)
                .plateNumber(plateNumber).build());
        
        if (res.size() == 1) {
            Policy policy = Policy.builder().
                    orderId(res.get(0).getOrderId()).
                    build();
            policyRepository.updatePolicy(policy);
            isMatch = true;
        }
        if (res.size() >= 2) {
            for (DrivingLicense item : res) {
                List<Policy> policies = policyRepository.selectPolicyByOrderId(item.getOrderId());
                if (policies.size() == 0) {
                    Policy policy = Policy.builder().id(policyId).orderId(item.getOrderId()).build();
                    policyRepository.updatePolicy(policy);
                    isMatch = true;
                    break;
                }
            }
        }
        if (!isMatch) {
            List<Certificate> res2 = certificateRepository.getCertificateByPolicyInfo(Certificate.builder().engine(engine).frame(frame).build());
            if (res2.size() == 0) {
                throw new OCRException();
            }
            if (res2.size() == 1) {
                Policy policy = Policy.builder().
                        orderId(res2.get(0).getOrderId()).
                        build();
                policyRepository.updatePolicy(policy);
                isMatch = true;
            }
            if (res2.size() >= 2) {
                for (Certificate item : res2) {
                    List<Policy> policies = policyRepository.selectPolicyByOrderId(item.getOrderId());
                    if (policies.size() == 0) {
                        Policy policy = Policy.builder().id(policyId).orderId(item.getOrderId()).build();
                        policyRepository.updatePolicy(policy);
                        isMatch = true;
                        break;
                    }
                }
            }
        }
        if (!isMatch) {
            throw new OCRException();
        }
    }
    
    public PolicyDealImpl(String filename, byte[] file, PolicyRepository policyRepository, DrivingLicenseRepository drivingLicenseRepository, CertificateRepository certificateRepository) {
        this.filename = filename;
        this.file = file;
        this.policyRepository = policyRepository;
        this.drivingLicenseRepository = drivingLicenseRepository;
        this.certificateRepository = certificateRepository;
    }
    
    @Override
    public void run() {
        try {
            createItemInDB();
            putPolicyToOSS();
            doOCR();
            matchOrder();
        } catch (OCRException e) {
            logger.warn("OCRException", e);
        } catch (IOException e) {
            logger.warn("IOException", e);
        } catch (Exception e) {
            logger.warn("Exception", e);
        }
    }
}

@Component
public class OcrInfoImpl implements OcrInfoService {
    @Resource
    private IdCardRepository idCardRepository;
    
    @Resource
    private BusinessLicenseRepository businessLicenseRepository;
    
    @Resource
    private DrivingLicenseRepository drivingLicenseRepository;
    
    @Resource
    private CertificateRepository certificateRepository;
    
    @Resource
    private PolicyRepository policyRepository;
    
    @Resource
    private OtherFileRepository otherFileRepository;
    
    private final ExecutorService executorService = new ThreadPoolExecutor(
            4,
            10,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>()
    );
    
    private String getPolicyName(String originName) {
        final String[] splitNames = originName.split("\\.");
        return splitNames[splitNames.length - 2] + UUID.randomUUID() + ".pdf";
    }
    
    private Map<String, byte[]> unzipFile(MultipartFile file) throws IOException {
        Map<String, byte[]> streamMap = new HashMap<>();
        ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
        ZipEntry zipEntry;
        
        while ((zipEntry = zipInputStream.getNextEntry()) != null && !zipEntry.isDirectory()) {
            String name = "policy/" + getPolicyName(zipEntry.getName());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            
            byte[] bytes = new byte[1024];
            int len;
            while ((len = zipInputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
            
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            zipInputStream.closeEntry();
            streamMap.put(name, byteArrayOutputStream.toByteArray());
        }
        
        zipInputStream.close();
        
        return streamMap;
    }
    
    @Override
    public IdCardResponseBody idCardInfoService(String url) throws IOException, OCRException {
        final String token = GetOcrToken.getAuthToken();
        final IdCardResponse response = OcrInfoGetter.idCard(url, token);
        
        final IdCardResponse.WordsResult wordsResult = response.getWordsResult();
        IdCard idCard = IdCard.builder().
                url(url).
                name(wordsResult.getName().getWords()).
                address(wordsResult.getAddress().getWords()).
                number(wordsResult.getNumber().getWords()).
                build();
        
        idCardRepository.createIdCard(idCard);
        
        return IdCardResponseBody.builder().
                id(idCard.getId()).
                name(idCard.getName()).
                address(idCard.getAddress()).
                number(idCard.getNumber()).
                build();
    }
    
    @Override
    public BusinessLicenseResponseBody businessInfoService(String url) throws IOException, OCRException {
        final String token = GetOcrToken.getAuthToken();
        final BusinessResponse response = OcrInfoGetter.businessLicense(url, token);
        
        final BusinessResponse.WordsResult wordsResult = response.getWordsResult();
        BusinessLicense businessLicense = BusinessLicense.builder().
                url(url).
                name(wordsResult.getCompanyName().getWords()).
                address(wordsResult.getAddress().getWords()).
                number(wordsResult.getCreditCode().getWords()).
                build();
        
        businessLicenseRepository.createBusinessLicense(businessLicense);
        
        return BusinessLicenseResponseBody.builder().
                id(businessLicense.getId()).
                name(businessLicense.getName()).
                address(businessLicense.getAddress()).
                number(businessLicense.getNumber()).
                build();
    }
    
    @Override
    public DrivingLicenseResponseBody drivingInfoService(String url) throws IOException, OCRException {
        final String token = GetOcrToken.getAuthToken();
        final DrivingLicenseResponse response = OcrInfoGetter.vehicleLicense(url, token);
        
        final DrivingLicenseResponse.WordsResult wordsResult = response.getWordsResult();
        DrivingLicense drivingLicense = DrivingLicense.builder().
                url(url).
                owner(wordsResult.getOwner().getWords()).
                plateNumber(wordsResult.getPlate().getWords()).
                engine(wordsResult.getEngine().getWords()).
                frame(wordsResult.getCarVerify().getWords()).
                type(wordsResult.getModel().getWords()).
                build();
        
        drivingLicenseRepository.createDrivingLicense(drivingLicense);
        
        return DrivingLicenseResponseBody.builder().
                id(drivingLicense.getId()).
                plate(drivingLicense.getPlateNumber()).
                engine(drivingLicense.getEngine()).
                frame(drivingLicense.getFrame()).
                type(drivingLicense.getType()).
                build();
    }
    
    @Override
    public CertificateResponseBody certificateInfoService(String url) throws IOException, OCRException {
        final String token = GetOcrToken.getAuthToken();
        final CertificateResponse response = OcrInfoGetter.vehicleCertificate(url, token);
        
        final CertificateResponse.WordsResult wordsResult = response.getWordsResult();
        Certificate certificate = Certificate.builder().
                url(url).
                engine(wordsResult.getEngineNo()).
                frame(wordsResult.getVinNo()).
                build();
        
        certificateRepository.createCertificate(certificate);
        
        return CertificateResponseBody.builder().
                id(certificate.getId()).
                engine(certificate.getEngine()).
                frame(certificate.getFrame()).
                build();
    }
    
    @Override
    public OtherFileResponseBody otherFileService(String url) {
        OtherFile otherFile = OtherFile.builder().
                url(url).
                build();
        
        otherFileRepository.createOtherFile(otherFile);
        
        return OtherFileResponseBody.builder().
                id(otherFile.getId()).
                build();
    }
    
    /**
     * 将保单任务提交到线程池中执行，经过写入数据库，识别生成投保单三个过程
     *
     * @param file 用户上传的文件，只能是 pdf 或者 zip
     * @throws IOException 文件 IO 异常
     */
    @Override
    public void policyRecordService(MultipartFile file) throws IOException {
        final String originName = file.getOriginalFilename();
        if (originName == null) {
            throw new FileTypeException();
        }
        
        final String[] splitNames = originName.split("\\.");
        // PDF 直接上传写入数据库
        if (splitNames[splitNames.length - 1].equals("pdf")) {
            String OSSPath = "policy/" + getPolicyName(originName);
            executorService.submit(new PolicyDealImpl(OSSPath, file.getBytes(), policyRepository, drivingLicenseRepository, certificateRepository));
            return;
        }
        
        // 将文件解压上传 Aliyun 或者直接上传，并写入数据库
        if (splitNames[splitNames.length - 1].equals("zip")) {
            Map<String, byte[]> streamMap = unzipFile(file);
            Set<Map.Entry<String, byte[]>> entrySet = streamMap.entrySet();
            for (Map.Entry<String, byte[]> entry : entrySet) {
                executorService.submit(new PolicyDealImpl(entry.getKey(), entry.getValue(), policyRepository, drivingLicenseRepository, certificateRepository));
            }
            
            return;
        }
        
        throw new FileTypeException();
    }
}
