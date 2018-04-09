package com.chenxin.houseparent.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface AdminDao {

    /**
     * 获取password
     *
     * @param adminName
     * @return
     */
    String getPasswordByAdminName(@Param("adminName") String adminName) throws Exception;

    /**
     * 获取用户的角色
     *
     * @param adminName
     * @return
     */
    List<JSONObject> listRolesPermissions(@Param("adminName") String adminName) throws Exception;

    /**
     * 用户注册
     *
     * @param adminName
     * @param password
     */
    void register(@Param("adminName") String adminName, @Param("password") String password) throws Exception;

    /**
     * 更新密码
     *
     * @param adminName
     * @param password
     */
    void updatePassword(@Param("adminName") String adminName, @Param("password") String password) throws Exception;

    /**
     * 查询admin账号是否存在
     *
     * @param username
     * @return
     */
    Integer getAdminNameByAdminName(@Param("username") String username) throws Exception;

    /**
     * 统计通知数量
     */
    Integer countNotification();

    /**
     * 查询最近8条数据
     *
     * @param pageSize
     * @return
     */
    List<JSONObject> listLastNotification(int pageSize);

    /**
     * 分页查询
     *
     * @return
     */
    List<JSONObject> listLastNotification1(int page, int pageSize);

    /**
     * 统计时间段内的数据数量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Integer countNotification1(Date startTime, Date endTime);

    /**
     * 查询时间内最近8条
     *
     * @param startTime
     * @param endTime
     * @param pageSize
     * @return
     */
    List<JSONObject> listLastNotification2(Date startTime, Date endTime, int pageSize);

    /**
     * 查新该段时间内某一页通知
     *
     * @param startTime
     * @param endTime
     * @param pageSize
     * @param start
     * @return
     */
    List<JSONObject> listLastNotification3(Date startTime, Date endTime, int start, int pageSize);

    /**
     * 获取该用户可以换的宿舍
     *
     * @param className
     * @return
     */
    List<JSONObject> getChangeOfDormitoryApply(@Param("className") String className);

    /**
     * 更新用户宿舍
     *
     * @param userName
     * @param roomNumber
     */
    void updateUserRoomNumber(@Param("username") String userName, @Param("roomNumber") String roomNumber);

    /**
     * 用户提交=更换宿舍申请
     *
     * @param param
     */
    void submitApplication(JSONObject param);


    /**
     * 获取adminName
     *
     * @param adminId
     * @return
     */
    String getAdminName(@Param("adminId") String adminId) throws Exception;

    /**
     * 统计楼栋数量
     *
     * @return
     */
    Integer countBuildingNumber() throws Exception;

    /**
     * 查询特定页数据
     *
     * @param startRow
     * @param pageSize
     * @return
     */
    List<JSONObject> listBuilding(@Param("startRow") int startRow, @Param("pageSize") int pageSize);

    /**
     * 更新楼栋
     *
     * @param param
     * @throws Exception
     */
    void updateBuilding(JSONObject param) throws Exception;

    /**
     * 删除building
     *
     * @param param
     */
    void deleteBuilding(JSONObject param);

    /**
     * 添加楼栋
     *
     * @param param
     */
    void addBuilding(List<JSONObject> param) throws Exception;


    /**
     * 统计可用的房间数
     *
     * @param param
     * @return
     */
    Integer countRoomNumber(JSONObject param) throws Exception;

    /**
     * 查询特定页数据
     *
     * @param param
     * @return
     */
    List<JSONObject> listRoom(JSONObject param);

    /**
     * 更新宿舍
     *
     * @param param
     * @throws Exception
     */
    void updateRoom(JSONObject param) throws Exception;

    /**
     * 删除宿舍
     *
     * @param param
     */
    void deleteRoom(JSONObject param);

    /**
     * 添加宿舍
     *
     * @param param
     */
    void addRoom(List<JSONObject> param) throws Exception;

    /**
     * 查询所有的楼栋
     *
     * @return
     */
    List<JSONObject> searchBuilding() throws Exception;

    /**
     * 统计用户数量
     *
     * @return
     */
    Integer countUserNumber() throws Exception;


    /**
     * 查询特定页数据
     *
     * @param startRow
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<JSONObject> listUser(@Param("startRow") int startRow, @Param("pageSize") int pageSize) throws Exception;

    /**
     * 更新用户
     *
     * @param param
     */
    void updateUser(JSONObject param) throws Exception;

    /**
     * 删除用户
     *
     * @param param
     */
    void deleteUser(JSONObject param) throws Exception;

    /**
     * 添加用户
     *
     * @param param
     */
    void addUser(List<JSONObject> param) throws Exception;

    /**
     * 统计员工数量
     *
     * @return
     */
    Integer countEmployeeNumber() throws Exception;


    /**
     * 查询特定员工页数据
     *
     * @param startRow
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<JSONObject> listEmployee(@Param("startRow") int startRow, @Param("pageSize") int pageSize) throws Exception;

    /**
     * 更新员工
     *
     * @param param
     */
    void updateEmployee(JSONObject param) throws Exception;

    /**
     * 删除员工
     *
     * @param param
     */
    void deleteEmployee(JSONObject param) throws Exception;

    /**
     * 添加员工
     *
     * @param param
     */
    void addEmployee(List<JSONObject> param) throws Exception;

    /**
     * 有田间查询用户
     *
     * @param param
     * @return
     */
    List<JSONObject> listUserByCondition(JSONObject param) throws Exception;

    /**
     * 统计满足条件的用户数量
     *
     * @param param
     * @return
     */
    Integer countUserNumberByCondition(JSONObject param) throws Exception;

    /**
     * 查询所有房间
     *
     * @return
     */
    List<JSONObject> searchRoom() throws Exception;

    /**
     * 分析楼栋住宿情况
     *
     * @return
     */
    List<JSONObject> analysisUser() throws Exception;

    /**
     * 添加外来物品登记
     *
     * @param param
     */
    void addInOutRegister(List<JSONObject> param) throws Exception;

    /**
     * 统计进出楼物品登记数
     *
     * @return
     */
    Integer countInOutRegisterNumber() throws Exception;

    /**
     * 列举特定页进出楼登记
     *
     * @param startRow
     * @param pageSize
     * @return
     */
    List<JSONObject> listInOutRegister(@Param("startRow") int startRow, @Param("pageSize") int pageSize) throws Exception;

    /**
     * 更新进出楼物品登记
     *
     * @param param
     */
    void updateInOutRegister(JSONObject param) throws Exception;

    /**
     * 删除进出楼物品登记记录
     *
     * @param param
     */
    void deleteInOutRegister(JSONObject param) throws Exception;

    /**
     * 统计人员来访记录数
     *
     * @param param
     * @return
     */
    Integer countMockSurveyNumber(JSONObject param) throws Exception;

    /**
     * 列举特定页人员来访记录
     *
     * @param param
     * @return
     * @throws Exception
     */
    List<JSONObject> listMockSurvey(JSONObject param) throws Exception;

    /**
     * 更新人员来访记录
     *
     * @param param
     */
    void updateMockSurvey(JSONObject param) throws Exception;

    /**
     * 结束人员来访
     *
     * @param param
     */
    void mockSurveyConcludeVisit(JSONObject param) throws Exception;

    /**
     * 删除人员来访记录
     *
     * @param param
     */
    void deleteMockSurvey(JSONObject param) throws Exception;

    /**
     * 添加人员来访登记
     *
     * @param param
     */
    void addMockSurvey(List<JSONObject> param);
}
