package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.*;
import cn.wghtstudio.insurance.dao.repository.*;
import cn.wghtstudio.insurance.exception.FileTypeException;
import cn.wghtstudio.insurance.exception.OCRException;
import cn.wghtstudio.insurance.service.OcrInfoService;
import cn.wghtstudio.insurance.service.entity.*;
import cn.wghtstudio.insurance.util.AliyunUtil;
import cn.wghtstudio.insurance.util.ocr.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class PolicyDealImpl implements Runnable {
    private final String filename;

    private final byte[] file;

    private final PolicyRepository policyRepository;

    private void createItemInDB() {
        Policy policy = Policy.builder().
                url("https://versicherung.oss-cn-beijing.aliyuncs.com/" + filename).
                build();
        policyRepository.createPolicy(policy);
    }

    private void putPolicyToOSS() {
        AliyunUtil.putObject(filename, new ByteArrayInputStream(file));
    }

    public PolicyDealImpl(String filename, byte[] file, PolicyRepository policyRepository) {
        this.filename = filename;
        this.file = file;
        this.policyRepository = policyRepository;
    }

    @Override
    public void run() {
        createItemInDB();

        putPolicyToOSS();
    }
}

@Component
public class OcrInfoImpl implements OcrInfoService {
    @Resource
    private OcrInfoGetter infoGetter;

    @Resource
    private GetOcrToken getOcrToken;

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
        final String token = getOcrToken.getAuthToken();
        final IdCardResponse response = infoGetter.idCard(url, token);

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
        final String token = getOcrToken.getAuthToken();
        final BusinessResponse response = infoGetter.businessLicense(url, token);

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
        final String token = getOcrToken.getAuthToken();
        final DrivingLicenseResponse response = infoGetter.vehicleLicense(url, token);

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
        final String token = getOcrToken.getAuthToken();
        final CertificateResponse response = infoGetter.vehicleCertificate(url, token);

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
            executorService.submit(new PolicyDealImpl(OSSPath, file.getBytes(), policyRepository));
            return;
        }

        // 将文件解压上传 Aliyun 或者直接上传，并写入数据库
        if (splitNames[splitNames.length - 1].equals("zip")) {
            Map<String, byte[]> streamMap = unzipFile(file);
            Set<Map.Entry<String, byte[]>> entrySet = streamMap.entrySet();
            for (Map.Entry<String, byte[]> entry : entrySet) {
                executorService.submit(new PolicyDealImpl(entry.getKey(), entry.getValue(), policyRepository));
            }

            return;
        }

        throw new FileTypeException();
    }

//    public static String getMatcher(String regex, String source) {
//        String result = "";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(source);
//        while (matcher.find()) {
//            result = matcher.group();
//        }
//        return result;
//    }


//    public InsurancepolicyResponseBody insurance(String url) throws IOException, OCRException {
//        final String token = getOcrToken.getAuthToken();
//        final InsurancepolicyResponse response = infoGetter.vehicleInsurance(url, token);
//        final InsurancepolicyResponse.WordsResult wordsResult = response.getWordsResult();
//        String number = null, plateNumber = null, frame = null, engineNumber = null;
//        String pattern1 = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
//        String pattern2 = "[A-Z]{4}[0-9]{16}";
//        String pattern3 = "[A-Z]{3}[A-z0-9]{14}";
//        String pattern4 = "(发动机号:[A-Z0-9]{6,11})";
//        String pattern5 = "[A-Z0-9]{6,11}";
//        for (String word : wordsResult.getWords()) {
//            String tmpWord = getMatcher(pattern2, word);
//            if (!Objects.equals(tmpWord, "")) {
//                number = tmpWord;
//            }
//            tmpWord = getMatcher(pattern1, word);
//            if (!Objects.equals(tmpWord, "")) {
//                plateNumber = tmpWord;
//            }
//            tmpWord = getMatcher(pattern3, word);
//            if (!Objects.equals(tmpWord, "")) {
//                frame = tmpWord;
//            }
//            tmpWord = getMatcher(pattern4, word);
//            if (!Objects.equals(tmpWord, "")) {
//                engineNumber = tmpWord;
//            }
//        }
//
//        String tmpWord = getMatcher(pattern5, engineNumber);
//        if (!Objects.equals(tmpWord, "")) {
//            engineNumber = tmpWord;
//        }
//
//        Policy policy = Policy.builder().
//                url(url).
//                number(plateNumber).
//                frame(frame).
//                engine(engineNumber).
//                plate(plateNumber).
//                build();
//
//        policyRepository.createPolicy(policy);
//
//        return InsurancepolicyResponseBody.builder().
//                number(number).
//                plateNumber(plateNumber).
//                frame(frame).
//                engine(engineNumber).
//                build();
//    }
}
