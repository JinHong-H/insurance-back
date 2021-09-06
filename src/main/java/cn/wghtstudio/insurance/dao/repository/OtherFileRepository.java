package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.OtherFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OtherFileRepository {
    void createOtherFile(OtherFile otherFile);

    void updateOtherFiles(Map<String, Object> params);

    void deleteOtherFiles(int id);

    List<OtherFile> getOtherFilesByOrderId(int orderId);
}
