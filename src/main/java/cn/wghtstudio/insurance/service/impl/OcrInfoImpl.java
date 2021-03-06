package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.*;
import cn.wghtstudio.insurance.dao.repository.*;
import cn.wghtstudio.insurance.exception.FileTypeException;
import cn.wghtstudio.insurance.exception.OCRException;
import cn.wghtstudio.insurance.exception.PdfMakeErrorException;
import cn.wghtstudio.insurance.service.OcrInfoService;
import cn.wghtstudio.insurance.service.entity.*;
import cn.wghtstudio.insurance.util.AliyunUtil;
import cn.wghtstudio.insurance.util.ocr.*;
import cn.wghtstudio.insurance.util.pdf.*;
import com.itextpdf.text.DocumentException;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@Builder
class PolicyDealParams {
    private int id;
    private String name;
    private byte[] file;
}

class PolicyDealImpl implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PolicyDealImpl.class);

    private final Integer policyId;

    private final String OSSPath;

    private final byte[] file;

    private final OrderRepository orderRepository;

    private final PolicyRepository policyRepository;

    private final DrivingLicenseRepository drivingLicenseRepository;

    private final CertificateRepository certificateRepository;

    private final OverInsurancePolicyRepository overInsurancePolicyRepository;

    private final OverInsurancePolicyPicRepository overInsurancePolicyPicRepository;

    private String number = null, plateNumber = null, frame = null, engine = null;

    private Integer orderId = null;

    private final Map<String, String> dataMap = new HashMap<>();

    private final Map<String, byte[]> imgMap = new HashMap<>();


    @Nullable
    private String getInsuranceNumber(String words) {
        Pattern pattern = Pattern.compile("????????????\\s?[:???]\\s?([A-Z]{4}[0-9]{18})");
        Matcher matcher = pattern.matcher(words);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Nullable
    private String getPlateNumber(String words) {
        Pattern pattern = Pattern.compile("?????????\\s?[:???]\\s?([?????????????????????????????????????????????????????????????????????????????????????????????](([A-HJ-Z][A-HJ-NP-Z0-9]{5})|([A-HJ-Z](([DF][A-HJ-NP-Z0-9][0-9]{4})|([0-9]{5}[DF])))|([A-HJ-Z][A-D0-9][0-9]{3}???)))|([0-9]{6}???)|((([????????????????????????????????????]A)|???B|???D|???E|???H)[0-9]{4}???)|(WJ[??????????????????????????????????????????????????????????????????????????????????????????????????][0-9]{4}[TDSHBXJ0-9])|([VKHBSLJNGCE][A-DJ-PR-TVY][0-9]{5})");
        Matcher matcher = pattern.matcher(words);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Nullable
    private String getFrameNumber(String words) {
        Pattern pattern = Pattern.compile("?????????\\s?[:???]\\s?([A-Z]{3}[A-z0-9]{14})");
        Matcher matcher = pattern.matcher(words);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Nullable
    private String getEngineNumber(String words) {
        Pattern pattern = Pattern.compile("????????????\\s?[:???]\\s?([A-Z0-9]{6,11})");
        Matcher matcher = pattern.matcher(words);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Nullable
    private String getName(String words) {
        Pattern pattern = Pattern.compile("(??????\\s?(/????????????)?[:???]?)");
        Matcher matcher = pattern.matcher(words);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Nullable
    private List<String> getTime(String words) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d{4}???\\d{2}???\\d{2}???");
        Matcher matcher = pattern.matcher(words);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        if (result.size() == 0) {
            return null;
        }
        return result;
    }

    @Nullable
    private String getCompanyNumber(String words) {
        Pattern pattern = Pattern.compile("(????????????(\\s)?[:???]?)");
        Matcher matcher = pattern.matcher(words);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private boolean judgeOfficialMode(int orderId) {
        return orderRepository.getOrderById(Order.builder().id(orderId).build()).getFileType() > 1;
    }

    private String getPolicyName(String originName) {
        final String[] splitNames = originName.split("\\.");
        return splitNames[splitNames.length - 2] + UUID.randomUUID() + ".pdf";
    }

    private String getOverInsurancePolicyName(String originName) {
        final String[] splitNames = originName.replace("policy/", "").split("\\.");
        return splitNames[splitNames.length - 2] + UUID.randomUUID() + ".pdf";
    }

    private String getOverInsurancePolicyPicBaseName(String originName) {
        final String[] splitNames = originName.replace("policy/", "").split("\\.");
        return splitNames[splitNames.length - 2] + UUID.randomUUID();
    }

    private void putPolicyToOSS() {
        Policy policyBeforePut = Policy.builder().
                id(policyId).
                processType(1).
                build();

        policyRepository.updatePolicy(policyBeforePut);

        AliyunUtil.putObject(OSSPath, new ByteArrayInputStream(file));

        Policy policyAfterPut = Policy.builder().
                id(policyId).
                url("https://versicherung.oss-cn-beijing.aliyuncs.com/" + OSSPath).
                build();

        policyRepository.updatePolicy(policyAfterPut);
    }

    private void generateSeal(String name) throws IOException {
        if (name == null) {
            return;
        }
        Seal seal;

        // ?????????
        SealFont mainFont = SealFont.builder().
                isBold(false).
                fontFamily("??????").
                marginSize(25).
                fontText(name).
                fontSize(45).
                fontSpace(35.0).
                build();

        // ????????????
        SealFont centerFont = SealFont.builder().
                isBold(true).
                fontFamily("??????").
                fontText("???").
                fontSize(75).
                build();

        // ??????????????????
        SealConfiguration configuration = SealConfiguration.builder().
                mainFont(mainFont).
                centerFont(centerFont).
                imageSize(300).
                backgroundColor(new Color(255, 65, 65, 207)).
                borderCircle(new SealCircle(6, 140, 140)).
                build();

        seal = new OfficialSeal(configuration);
        byte[] sealBytes = seal.draw();

        imgMap.put("seal", sealBytes);
    }

    private void doOCR() throws IOException {
        Policy policyBeforeOCR = Policy.builder().
                id(policyId).
                processType(2).
                build();

        policyRepository.updatePolicy(policyBeforeOCR);

        String b64encoded = Base64.getEncoder().encodeToString(file);
        final String token = GetOcrToken.getAuthToken();
        List<String> name = new ArrayList<>(), company = new ArrayList<>(), time = new ArrayList<>();

        final InsurancePolicyResponse response = OcrInfoGetter.vehicleInsurance(b64encoded, token);
        final List<InsurancePolicyResponse.WordsResult> wordsResultList = response.getWordsResult();

        // ??? OCR ??????????????????????????? ????????? ????????? ????????????
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

            if (name.size() < 2) {
                String tmp = getName(words);
                if (tmp != null) {
                    name.add(words.replace(tmp, ""));
                }
            }

            if (company.size() < 2) {
                String tmp = getCompanyNumber(words);
                if (tmp != null) {
                    company.add(words.replace(tmp, ""));
                }
            }

            if (time.size() < 2) {
                List<String> tmp = getTime(words);
                if (tmp != null) {
                    time = tmp;
                }
            }
        }

        // ?????? pdf ????????????????????????
        for (String item : name) {
            if (!dataMap.containsKey("person1")) {
                dataMap.put("person1", item);
                continue;
            }
            dataMap.put("person2", item);
        }

        for (String item : company) {
            if (!dataMap.containsKey("person1id")) {
                dataMap.put("person1id", item);
                continue;
            }
            dataMap.put("person2id", item);
        }

        for (String item : time) {
            if (!dataMap.containsKey("mon1")) {
                dataMap.put("year1", item.substring(0, 4));
                dataMap.put("mon1", item.substring(5, 7));
                dataMap.put("day1", item.substring(8, 10));
                continue;
            }
            dataMap.put("year2", item.substring(0, 4));
            dataMap.put("mon2", item.substring(5, 7));
            dataMap.put("day2", item.substring(8, 10));
        }

        String allInfos = "";
        if (plateNumber != null) {
            allInfos += "?????????:" + plateNumber;
        }

        if (frame != null) {
            allInfos += "?????????:" + frame;
        }

        if (engine != null) {
            allInfos += "????????????:" + engine;
        }

        dataMap.put("allinfo", allInfos);

        Policy policy = Policy.builder().
                id(policyId).
                number(number).
                build();
        policyRepository.updatePolicy(policy);
    }

    private boolean checkPolicyAndUpdate(Integer orderId) {
        List<Policy> policies = policyRepository.selectPolicyByOrderId(orderId);
        if (policies.size() == 0) {
            Policy policy = Policy.builder().id(policyId).orderId(orderId).build();
            policyRepository.updatePolicy(policy);
            return true;
        }
        return false;
    }

    private void matchOrder() {
        // ?????????0 ????????????????????????1 ?????????????????? ?????????????????? ??????2???????????????????????????????????????????????????orderID???????????????????????????
        List<DrivingLicense> drivingLicenseList = drivingLicenseRepository.getDrivingLicenseByPolicyInfo(
                DrivingLicense.builder().
                        engine(engine).
                        frame(frame).
                        plateNumber(plateNumber).
                        build()
        );

        for (DrivingLicense item : drivingLicenseList) {
            if (checkPolicyAndUpdate(item.getOrderId())) {
                orderId = item.getOrderId();
                return;
            }
        }


        List<Certificate> certificateList = certificateRepository.getCertificateByPolicyInfo(
                Certificate.builder().
                        engine(engine).
                        frame(frame).
                        build()
        );

        for (Certificate item : certificateList) {
            if (checkPolicyAndUpdate(item.getOrderId())) {
                orderId = item.getOrderId();
                return;
            }
        }


        throw new OCRException();
    }

    private void generateOverInsurancePolicy() throws IOException, PdfMakeErrorException {
        if (dataMap.size() < 11) {
            throw new OCRException();
        }
        // ????????????
        boolean isOfficial = judgeOfficialMode(orderId);
        if (isOfficial) {
            generateSeal(dataMap.get("person1"));
        }

        byte[] data;
        try {
            data = new PdfMaker(dataMap, imgMap).generate(isOfficial);
        } catch (DocumentException | IOException e) {
            System.out.println(e);
            throw new PdfMakeErrorException();
        }

        String overInsurancePolicyName = getOverInsurancePolicyName(OSSPath);
        String overInsurancePolicyPathName = "OverInsurancePolicy/" + overInsurancePolicyName;
        AliyunUtil.putObject(overInsurancePolicyPathName, new ByteArrayInputStream(data));
        OverInsurancePolicy overInsurancePolicy = OverInsurancePolicy.builder().
                name(overInsurancePolicyName).
                url("https://versicherung.oss-cn-beijing.aliyuncs.com/" + overInsurancePolicyPathName).
                orderId(orderId).
                build();
        overInsurancePolicyRepository.createOverInsurancePolicy(overInsurancePolicy);

        List<byte[]> picData;
        try {
            picData = PdfMaker.pdfToImage(data);
        } catch (IOException e) {
            throw new PdfMakeErrorException();
        }

        if (picData.size() == 0) {
            throw new PdfMakeErrorException();
        }

        String overInsurancePolicyPicFileName = getOverInsurancePolicyPicBaseName(OSSPath);
        String overInsurancePolicyPicPathBaseName = "OverInsurancePolicyPic/" + overInsurancePolicyPicFileName;
        for (int i = 0; i < picData.size(); i++) {
            String finalName = overInsurancePolicyPicFileName + i + ".png";
            String finalPathName = overInsurancePolicyPicPathBaseName + i + ".png";
            AliyunUtil.putObject(finalPathName, new ByteArrayInputStream(picData.get(i)));
            OverInsurancePolicyPic overInsurancePolicyPic = OverInsurancePolicyPic.builder().
                    name(finalName).
                    url("https://versicherung.oss-cn-beijing.aliyuncs.com/" + finalPathName).
                    orderId(orderId).
                    build();
            overInsurancePolicyPicRepository.createOverInsurancePolicyPic(overInsurancePolicyPic);
        }
    }

    public PolicyDealImpl(PolicyDealParams params, PolicyRepository policyRepository, DrivingLicenseRepository drivingLicenseRepository, CertificateRepository certificateRepository, OverInsurancePolicyRepository overInsurancePolicyRepository, OverInsurancePolicyPicRepository overInsurancePolicyPicRepository, OrderRepository orderRepository) {
        this.OSSPath = "policy/" + getPolicyName(params.getName());
        this.file = params.getFile();
        this.policyId = params.getId();

        this.policyRepository = policyRepository;
        this.drivingLicenseRepository = drivingLicenseRepository;
        this.certificateRepository = certificateRepository;
        this.overInsurancePolicyRepository = overInsurancePolicyRepository;
        this.overInsurancePolicyPicRepository = overInsurancePolicyPicRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run() {
        try {
            putPolicyToOSS();
            doOCR();
            matchOrder();
            generateOverInsurancePolicy();
            Policy policy = Policy.builder().
                    id(policyId).
                    processType(4).
                    build();
            policyRepository.updatePolicy(policy);
        } catch (PdfMakeErrorException e) {
            logger.warn("PdfMakeErrorException", e);
            Policy policy = Policy.builder().
                    id(policyId).
                    processType(5).
                    build();
            policyRepository.updatePolicy(policy);
        } catch (OCRException e) {
            logger.warn("OCRException", e);
            Policy policy = Policy.builder().
                    id(policyId).
                    processType(6).
                    build();
            policyRepository.updatePolicy(policy);
        } catch (IOException e) {
            logger.warn("IOException", e);
            Policy policy = Policy.builder().
                    id(policyId).
                    processType(7).
                    build();
            policyRepository.updatePolicy(policy);
        } catch (Exception e) {
            logger.warn("Exception", e);
            Policy policy = Policy.builder().
                    id(policyId).
                    processType(8).
                    build();
            policyRepository.updatePolicy(policy);
        }
    }
}

@Component
public class OcrInfoImpl implements OcrInfoService {
    @Resource
    private OrderRepository orderRepository;

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

    @Resource
    private OverInsurancePolicyRepository overInsurancePolicyRepository;

    @Resource
    private OverInsurancePolicyPicRepository overInsurancePolicyPicRepository;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private List<PolicyDealParams> unzipFile(MultipartFile file) throws IOException {
        List<PolicyDealParams> params = new ArrayList<>();

        try (ZipArchiveInputStream zip = new ZipArchiveInputStream(file.getInputStream())) {
            ZipArchiveEntry zipArchiveEntry;
            while ((zipArchiveEntry = zip.getNextZipEntry()) != null && !zipArchiveEntry.isDirectory()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                byte[] bytes = new byte[1024];
                int len;
                while ((len = zip.read(bytes)) != -1) {
                    byteArrayOutputStream.write(bytes, 0, len);
                }

                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
                params.add(PolicyDealParams.builder().
                        name(zipArchiveEntry.getName()).
                        file(byteArrayOutputStream.toByteArray()).
                        build());
            }
        }

        return params;
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
                carType(wordsResult.getCarName()).
                engine(wordsResult.getEngineNo()).
                frame(wordsResult.getVinNo()).
                build();

        certificateRepository.createCertificate(certificate);

        return CertificateResponseBody.builder().
                id(certificate.getId()).
                carType(certificate.getCarType()).
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
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param file ????????????????????????????????? pdf ?????? zip
     * @throws IOException ?????? IO ??????
     */
    @Override
    public void policyRecordService(MultipartFile file) throws IOException {
        final String originName = file.getOriginalFilename();
        if (originName == null) {
            throw new FileTypeException();
        }

        final String[] splitNames = originName.split("\\.");
        // PDF ???????????????????????????
        if (splitNames[splitNames.length - 1].equals("pdf")) {
            // ??????????????? ???????????????
            List<Policy> policy = List.of(Policy.builder().name(originName).build());
            policyRepository.createPolicy(policy);
            PolicyDealParams params = PolicyDealParams.builder().
                    id(policy.get(0).getId()).
                    name(originName).
                    file(file.getBytes()).
                    build();
            executorService.submit(new PolicyDealImpl(params, policyRepository, drivingLicenseRepository, certificateRepository, overInsurancePolicyRepository, overInsurancePolicyPicRepository, orderRepository));
            return;
        }

        // ????????????????????? Aliyun ???????????????????????????????????????
        if (splitNames[splitNames.length - 1].equals("zip")) {
            // ???????????????
            List<PolicyDealParams> params = unzipFile(file);

            // ??????????????? ???????????????
            List<Policy> policies = params.stream().
                    map((item) -> Policy.builder().name(item.getName()).build()).
                    collect(Collectors.toList());
            policyRepository.createPolicy(policies);

            for (int i = 0; i < params.size(); i++) {
                PolicyDealParams item = params.get(i);
                item.setId(policies.get(i).getId());
            }

            // ??????????????????
            for (PolicyDealParams item : params) {
                executorService.submit(new PolicyDealImpl(item, policyRepository, drivingLicenseRepository, certificateRepository, overInsurancePolicyRepository, overInsurancePolicyPicRepository, orderRepository));
            }

            return;
        }

        throw new FileTypeException();
    }
}
