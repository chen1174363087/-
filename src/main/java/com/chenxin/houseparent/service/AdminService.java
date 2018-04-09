package com.chenxin.houseparent.service;

import com.alibaba.fastjson.JSONObject;
import com.chenxin.houseparent.dao.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {
    @Autowired
    private AdminDao adminDao;

    /**
     * 用户注册
     *
     * @param adminName
     * @param password
     */
    public void register(String adminName, String password) throws Exception {
        adminDao.register(adminName, password);
    }

    /**
     * 通过username查密码
     *
     * @param adminName
     * @return
     */
    public String getPasswordByAdminName(String adminName) throws Exception {
        return adminDao.getPasswordByAdminName(adminName);
    }

    /**
     * 列举用户角色及权限
     *
     * @param adminName
     * @return
     */
    public Map<String, Set<String>> listRolesPermissions(String adminName) throws Exception {
        List<JSONObject> rolesPermissions = adminDao.listRolesPermissions(adminName);
        Map<String, Set<String>> rolesPermissionsMap = new HashMap<>();
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        JSONObject temp = null;
        for (int i = 0, size = rolesPermissions.size(); i < size; i++) {
            temp = rolesPermissions.get(i);
            roles.add(temp.getString("roleName"));
            permissions.add(temp.getString("permissionName"));
        }
        rolesPermissionsMap.put("roles", roles);
        rolesPermissionsMap.put("permissions", permissions);
        //TODO 缓存
        return rolesPermissionsMap;
    }

    /**
     * 更改密码
     *
     * @param adminNname
     * @param password
     */
    public void updatePassword(String adminNname, String password) throws Exception {
        adminDao.updatePassword(adminNname, password);
    }

    /**
     * 查询用户是否存在
     *
     * @param username
     * @return
     */
    public Integer getAdminNameByAdminName(String username) throws Exception {
        return adminDao.getAdminNameByAdminName(username);
    }

    /**
     * 统计通知数量
     */
    public Integer countNotification() {
        return adminDao.countNotification();
    }

    /**
     * 查询最近8条数据
     *
     * @param pageSize
     */
    public List<JSONObject> listLastNotification(int pageSize) {
        return adminDao.listLastNotification(pageSize);
    }

    /**
     * 分页查询
     *
     * @param pageSize
     * @param page
     * @return
     */
    public List<JSONObject> listLastNotification1(int pageSize, int page) {
        return adminDao.listLastNotification1(pageSize * (page - 1), pageSize);
    }

    /**
     * 统计时间内的通知条数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public Integer countNotification1(Date startTime, Date endTime) {
        return adminDao.countNotification1(startTime, endTime);
    }

    /**
     * 查询时间内最近8条
     *
     * @param startTime
     * @param endTime
     * @param pageSize
     * @return
     */
    public List<JSONObject> listLastNotification2(Date startTime, Date endTime, int pageSize) {
        return adminDao.listLastNotification2(startTime, endTime, pageSize);
    }

    /**
     * 查新该段时间内某一页通知
     *
     * @param startTime
     * @param endTime
     * @param pageSize
     * @param page
     * @return
     */
    public List<JSONObject> listLastNotification3(Date startTime, Date endTime, int pageSize, int page) {
        return adminDao.listLastNotification3(startTime, endTime, pageSize * (page - 1), pageSize);
    }

    /**
     * 获取该用户可以换的宿舍
     *
     * @param className
     * @param
     * @return
     */
    public List<List<JSONObject>> getChangeOfDormitoryApply(String className) {
        List<JSONObject> usableRooms = adminDao.getChangeOfDormitoryApply(className);
        //找到人数小于4的宿舍
        List<List<JSONObject>> roomCategory = new ArrayList<>();
        while (usableRooms.size() > 0) {
            List<JSONObject> oneRoomUsers = new ArrayList<>();
            oneRoomUsers.add(usableRooms.get(0));
            JSONObject thisRoom = usableRooms.get(0);
            String roomNumber = thisRoom.getString("roomNumber");
            for (int i = 1; i < usableRooms.size(); ) {
                if (roomNumber.equals(usableRooms.get(i).getString("roomNumber"))) {
                    oneRoomUsers.add(usableRooms.get(i));
                    usableRooms.remove(i);
                } else if (usableRooms.size() > 0) {
                    i++;
                }
            }
            usableRooms.remove(0);
            roomCategory.add(oneRoomUsers);
        }
        //去掉已住满人
        for (int j = 0; j < roomCategory.size(); j++) {
            if (roomCategory.get(j).size() > 3) {
                roomCategory.remove(j);
            }
        }
        return roomCategory;
    }

    /**
     * 更新用户宿舍
     *
     * @param userName
     * @param roomNumber
     */
    public void updateUserRoomNumber(String userName, String roomNumber) {
        adminDao.updateUserRoomNumber(userName, roomNumber);
    }

    /**
     * 提交转宿舍申请
     *
     * @param param
     */
    public void submitApplication(JSONObject param) {
        adminDao.submitApplication(param);
    }

    /**
     * 获取姓名
     *
     * @param adminId
     * @return
     */
    public String getAdminName(String adminId) throws Exception {
        return adminDao.getAdminName(adminId);
    }

    /**
     * 统计楼栋数量
     *
     * @return
     */
    public Integer countBuildingNumber() throws Exception {
        return adminDao.countBuildingNumber();
    }

    /**
     * 查询特定页楼栋
     *
     * @param i
     * @param pageSize
     * @return
     */
    public List<JSONObject> listBuilding(int i, int pageSize) throws Exception {
        return adminDao.listBuilding((i - 1) * pageSize, pageSize);
    }

    /**
     * 更新楼栋
     *
     * @param param
     */
    public void updateBuilding(JSONObject param) throws Exception {
        adminDao.updateBuilding(param);
    }

    /**
     * 删除building
     *
     * @param param
     * @throws Exception
     */
    public void deleteBuilding(JSONObject param) throws Exception {
        adminDao.deleteBuilding(param);
    }

    /**
     * 添加楼栋
     *
     * @param param
     */
    public void addBuilding(List<JSONObject> param) throws Exception {
        adminDao.addBuilding(param);
    }

    /**
     * 统计可用的房间数量
     *
     * @param param
     * @return
     * @throws Exception
     */
    public Integer countRoomNumber(JSONObject param) throws Exception {
        return adminDao.countRoomNumber(param);
    }

    /**
     * 查询特定页宿舍
     *
     * @param param
     * @return
     * @throws Exception
     */
    public List<JSONObject> listRoom(JSONObject param) throws Exception {
        return adminDao.listRoom(param);
    }

    /**
     * 更新宿舍
     *
     * @param param
     */
    public void updateRoom(JSONObject param) throws Exception {
        adminDao.updateRoom(param);
    }

    /**
     * 删除宿舍
     *
     * @param param
     * @throws Exception
     */
    public void deleteRoom(JSONObject param) throws Exception {
        adminDao.deleteRoom(param);
    }

    /**
     * 添加宿舍
     *
     * @param param
     */
    public void addRoom(List<JSONObject> param) throws Exception {
        adminDao.addRoom(param);
    }

    /**
     * 查询所有的楼栋
     */
    public List<JSONObject> searchBuilding() throws Exception {
        return adminDao.searchBuilding();
    }

    /**
     * 统计用户数量
     *
     * @return
     */
    public Integer countUserNumber() throws Exception {
        return adminDao.countUserNumber();
    }

    /**
     * 查询特定页数据
     *
     * @param pageNow
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<JSONObject> listUser(int pageNow, int pageSize) throws Exception {
        return adminDao.listUser((pageNow - 1) * pageSize, pageSize);
    }

    /**
     * 更新用户
     *
     * @param param
     */
    public void updateUser(JSONObject param) throws Exception {
        adminDao.updateUser(param);
    }

    /**
     * 删除用户
     *
     * @param param
     */
    public void deleteUser(JSONObject param) throws Exception {
        adminDao.deleteUser(param);
    }

    /**
     * 添加用户
     *
     * @param param
     */
    public void addUser(List<JSONObject> param) throws Exception {
        adminDao.addUser(param);
    }


    /**
     * 统计员工数量
     *
     * @return
     */
    public Integer countEmployeeNumber() throws Exception {
        return adminDao.countEmployeeNumber();
    }

    /**
     * 查询特定页数据
     *
     * @param pageNow
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<JSONObject> listEmployee(int pageNow, int pageSize) throws Exception {
        return adminDao.listEmployee((pageNow - 1) * pageSize, pageSize);
    }

    /**
     * 更新用户
     *
     * @param param
     */
    public void updateEmployee(JSONObject param) throws Exception {
        adminDao.updateEmployee(param);
    }

    /**
     * 删除用户
     *
     * @param param
     */
    public void deleteEmployee(JSONObject param) throws Exception {
        adminDao.deleteEmployee(param);
    }

    /**
     * 添加用户
     *
     * @param param
     */
    public void addEmployee(List<JSONObject> param) throws Exception {
        adminDao.addEmployee(param);
    }

    /**
     * 有条件查询用户
     *
     * @param param
     * @return
     */
    public List<JSONObject> listUserByCondition(JSONObject param) throws Exception {
        return adminDao.listUserByCondition(param);
    }

    /**
     * 统计满足条件的用户数
     *
     * @param param
     * @return
     */
    public Integer countUserNumberByCondition(JSONObject param) throws Exception {
        return adminDao.countUserNumberByCondition(param);
    }

    /**
     * 分析楼栋住宿情况
     *
     * @return
     */
    public List<JSONObject> analysisUser() throws Exception {
        return adminDao.analysisUser();
    }

    /**
     * 添加物品登记
     *
     * @param param
     */
    public void addInOutRegister(List<JSONObject> param) throws Exception {
        adminDao.addInOutRegister(param);
    }

    /**
     * 统计进出物品记录数
     *
     * @return
     */
    public Integer countInOutRegisterNumber() throws Exception {
        return adminDao.countInOutRegisterNumber();
    }

    /**
     * 列举特定页进出楼登记
     *
     * @param startRow
     * @param pageSize
     * @return
     */
    public List<JSONObject> listInOutRegister(int startRow, int pageSize) throws Exception {
        return adminDao.listInOutRegister(startRow, pageSize);
    }

    /**
     * 更新进出楼登记
     *
     * @param param
     */
    public void updateInOutRegister(JSONObject param) throws Exception {
        adminDao.updateInOutRegister(param);
    }

    /**
     * 删除进出楼物品登记记录
     *
     * @param param
     */
    public void deleteInOutRegister(JSONObject param) throws Exception {
        adminDao.deleteInOutRegister(param);
    }

    /**
     * 统计来访记录
     *
     * @param param
     * @return
     */
    public Integer countMockSurveyNumber(JSONObject param) throws Exception {
        return adminDao.countMockSurveyNumber(param);
    }

    /**
     * 列举特定页来访记录
     *
     * @param param
     * @return
     * @throws Exception
     */
    public List<JSONObject> listMockSurvey(JSONObject param) throws Exception {
        return adminDao.listMockSurvey(param);
    }

    /**
     * 更新来访记录
     *
     * @param param
     */
    public void updateMockSurvey(JSONObject param) throws Exception {
        adminDao.updateMockSurvey(param);
    }

    /**
     * 结束来访
     *
     * @param param
     */
    public void mockSurveyConcludeVisit(JSONObject param) throws Exception {
        adminDao.mockSurveyConcludeVisit(param);
    }

    /**
     * 删除来访记录
     *
     * @param param
     */
    public void deleteMockSurvey(JSONObject param) throws Exception {
        adminDao.deleteMockSurvey(param);
    }

    /**
     * 添加人员来访登记
     *
     * @param param
     */
    public void addMockSurvey(List<JSONObject> param) throws Exception {
        adminDao.addMockSurvey(param);
    }
}
