package com.chenxin.houseparent.controller;

import com.alibaba.fastjson.JSONObject;
import com.chenxin.houseparent.service.AdminService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AdminApi {
    private final Logger logger = LoggerFactory.getLogger(AdminApi.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    @Qualifier("securityManager")
    private DefaultWebSecurityManager securityManager;

    @Autowired
    private AdminService adminService;

    /**
     * 查询Admin是否存在
     *
     * @param response
     * @param params
     */
    @RequestMapping(value = "/admin/check", method = RequestMethod.POST)
    public void check(HttpServletResponse response, @RequestBody JSONObject params) {
        String username = params.getString("username");
        Integer uid = 0;
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            uid = adminService.getAdminNameByAdminName(username);
            if (uid == null) {
                out.write("管理员不存在！");
            } else if (uid > 0) {

            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("管理员不存在！");
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 查询Admin是否存在
     *
     * @param response
     */
    @RequestMapping(value = "/admin/getInfo", method = RequestMethod.GET)
    public void getInfo(HttpServletResponse response) {
        Subject currentAdmin = SecurityUtils.getSubject();
        String adminId = (String) currentAdmin.getPrincipal();
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String adminName = adminService.getAdminName(adminId);
            writer.write(adminName);
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("管理员");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 管理员登陆
     *
     * @param
     * @param params
     */
    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public void login(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject params) {
        String picCode = (String) request.getSession().getAttribute("picCode");
        String checkCode = params.getString("picCode");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (!checkCode.equalsIgnoreCase(picCode)) {
                out.println("验证码错误");
                return;
            }
            String adminName = params.getString("username");
            String password = params.getString("password");
            logger.info("管理员" + adminName + "登陆！");
            // shiroLoginFailure:就是shiro异常类的全类名.
            password = DigestUtils.sha256Hex(password);
            String realPassword = adminService.getPasswordByAdminName(adminName);
            if (realPassword.equals(password)) {
                UsernamePasswordToken token = new UsernamePasswordToken(adminName, password);
                token.setRememberMe(true);
                Subject currentUser = SecurityUtils.getSubject();
                currentUser.login(token);
            } else {
                out.write("账号或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }

    }

    @RequestMapping(value = "/admin/updatePassword", method = RequestMethod.POST)
    public void updatePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject params) {
        String picCode = (String) request.getSession().getAttribute("picCode");
        String checkCode = params.getString("picCode");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (!checkCode.equalsIgnoreCase(picCode)) {
                out.println("验证码错误");
                return;
            }
            String adminName = params.getString("username");
            String password = params.getString("password");
            password = DigestUtils.sha256Hex(password);
            adminService.updatePassword(adminName, password);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * admin注册
     *
     * @param params
     */
    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    public void register(@RequestBody JSONObject params) {
        String adminName = params.getString("username");
        String password = params.getString("password");
        password = DigestUtils.sha256Hex(password);
        try {
            adminService.register(adminName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * admin注销
     */
    @RequestMapping(value = "/admin/logout", method = RequestMethod.GET)
    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
    }

    /**
     * 用户管理
     *
     * @param response
     */
    @RequestMapping(value = "/systemManage/userManage", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void userManage(HttpServletResponse response, @RequestBody JSONObject param) {
        int pageNow = param.getIntValue("pageNow");
        Integer userNumber = 0;
        int pageSize = 8;
        int pages = 0;
        List<JSONObject> users = null;
        PrintWriter writer = null;
        response.setContentType("text/html;charset=utf-8");
        try {
            writer = response.getWriter();
            userNumber = adminService.countUserNumber();
            if (userNumber % pageSize == 0) {
                pages = userNumber / pageSize;
            } else {
                pages = userNumber / pageSize + 1;
            }
            //查询首页数据
            if (pageNow == 0) {
                users = adminService.listUser(1, pageSize);
            } else {
                //查询特定页数据
                users = adminService.listUser(pageNow, pageSize);
            }
            users.get(0).put("pages", pages);
            writer.write(users.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            writer.write("一个用户也没有哦！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    @RequestMapping(value = "/systemManage/userManage/updateUser", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void updateUser(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.updateUser(param);
            writer.write("更新成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("更新失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 删除用户管理
     *
     * @param response
     */
    @RequestMapping(value = "/systemManage/userManage/deleteUser", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void deleteUser(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.deleteUser(param);
            writer.write("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("删除失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 添加用户
     *
     * @param response
     * @param param
     */
    @RequestMapping(value = "/systemManage/userManage/addUser", method = RequestMethod.POST)
    public void addUser(HttpServletResponse response, @RequestBody List<JSONObject> param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        if (param == null || param.isEmpty()) {
            return;
        }
        for (JSONObject user : param) {
            user.put("studentPassword", DigestUtils.sha256Hex(user.getString("studentId")));
        }
        try {
            writer = response.getWriter();
            adminService.addUser(param);
            writer.write("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("添加失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 员工管理
     *
     * @param response
     */
    @RequestMapping(value = "/apartmentManage/employee", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void employee(HttpServletResponse response, @RequestBody JSONObject param) {
        int pageNow = param.getIntValue("pageNow");
        Integer employeeNumber = 0;
        int pageSize = 8;
        int pages = 0;
        List<JSONObject> users = null;
        PrintWriter writer = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            writer = response.getWriter();
            employeeNumber = adminService.countEmployeeNumber();
            if (employeeNumber % pageSize == 0) {
                pages = employeeNumber / pageSize;
            } else {
                pages = employeeNumber / pageSize + 1;
            }
            //查询首页数据
            if (pageNow == 0) {
                users = adminService.listEmployee(1, pageSize);
            } else {
                //查询特定页数据
                users = adminService.listEmployee(pageNow, pageSize);
            }
            users.get(0).put("pages", pages);
            writer.write(users.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            writer.write("一个员工也没有哦！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 更新员工信息
     *
     * @param response
     */
    @RequestMapping(value = "/apartmentManage/employee/updateEmployee", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void updateEmployee(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.updateEmployee(param);
            writer.write("更新成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("更新失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 删除用户管理
     *
     * @param response
     */
    @RequestMapping(value = "/apartmentManage/employee/deleteEmployee", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void deleteEmployee(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.deleteEmployee(param);
            writer.write("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("删除失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 添加用户
     *
     * @param response
     * @param param
     */
    @RequestMapping(value = "/apartmentManage/employee/addEmployee", method = RequestMethod.POST)
    public void addEmployee(HttpServletResponse response, @RequestBody List<JSONObject> param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        if (param == null || param.isEmpty()) {
            return;
        }
        for (JSONObject employee : param) {
            employee.put("employeePassword", DigestUtils.sha256Hex(employee.getString("employeeId")));
        }
        try {
            writer = response.getWriter();
            adminService.addEmployee(param);
            writer.write("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("添加失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }


    /**
     * 列出楼栋
     *
     * @param response
     */
    @RequestMapping(value = "/apartmentManage/building", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void listBuilding(HttpServletResponse response, @RequestBody JSONObject param) {
        int pageNow = param.getIntValue("pageNow");
        Integer buildingNumber = 0;
        int pageSize = 8;
        int pages = 0;
        List<JSONObject> buildings = null;
        PrintWriter writer = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            writer = response.getWriter();
            buildingNumber = adminService.countBuildingNumber();
            if (buildingNumber % pageSize == 0) {
                pages = buildingNumber / pageSize;
            } else {
                pages = buildingNumber / pageSize + 1;
            }
            //查询首页数据
            if (pageNow == 0) {
                buildings = adminService.listBuilding(1, pageSize);
                buildings.get(0).put("pageNow", 1);
            } else {
                //查询特定页数据
                buildings = adminService.listBuilding(pageNow, pageSize);
                buildings.get(0).put("pageNow", pageNow);
            }
            buildings.get(0).put("pages", pages);
            writer.write(buildings.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            writer.write("一条信息也没有哦！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 删除楼栋
     *
     * @param response
     */
    @RequestMapping(value = "/apartmentManage/building/deleteBuilding", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void deleteBuilding(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.deleteBuilding(param);
            writer.write("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("删除失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 更新楼栋
     *
     * @param response
     */
    @RequestMapping(value = "/apartmentManage/building/updateBuilding", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void updateBuilding(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.updateBuilding(param);
            writer.write("更新成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("更新失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    @RequestMapping(value = "/apartmentManage/building/addBuilding", method = RequestMethod.POST)
    public void addBuilding(HttpServletResponse response, @RequestBody List<JSONObject> param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.addBuilding(param);
            writer.write("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("添加失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 列出房间
     *
     * @param response
     */
    @RequestMapping(value = "/apartmentManage/room", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void listRoom(HttpServletResponse response, @RequestBody JSONObject param) {
        int pageNow = param.getIntValue("pageNow");
        Integer RoomNumber = 0;
        int pageSize = 8;
        int pages = 0;
        List<JSONObject> rooms = null;
        PrintWriter writer = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            writer = response.getWriter();
            RoomNumber = adminService.countRoomNumber(param);
            if (RoomNumber % pageSize == 0) {
                pages = RoomNumber / pageSize;
            } else {
                pages = RoomNumber / pageSize + 1;
            }
            //查询首页数据
            param.put("pageSize", pageSize);
            if (pageNow == 0) {
                param.put("startRow", 0);
                rooms = adminService.listRoom(param);
                rooms.get(0).put("pageNow", 1);
            } else {
                //查询特定页数据
                param.put("startRow", (pageNow - 1) * pageSize);
                rooms = adminService.listRoom(param);
                rooms.get(0).put("pageNow", pageNow);
            }
            rooms.get(0).put("pages", pages);
            writer.write(rooms.toString());
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("一条信息也没有哦！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 删除宿舍
     *
     * @param response
     */
    @RequestMapping(value = "/apartmentManage/room/deleteRoom", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void deleteRoom(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.deleteRoom(param);
            writer.write("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("删除失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 更新房间
     *
     * @param response
     */
    @RequestMapping(value = "/apartmentManage/room/updateRoom", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void updateRoom(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.updateRoom(param);
            writer.write("更新成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("更新失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 添加房间
     *
     * @param response
     * @param param
     */
    @RequestMapping(value = "/apartmentManage/room/addRoom", method = RequestMethod.POST)
    public void addRoom(HttpServletResponse response, @RequestBody List<JSONObject> param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.addRoom(param);
            writer.write("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("添加失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 查询所有楼栋
     *
     * @param response
     */
    @RequestMapping(value = "/apartmentManage/building/searchBuilding", method = RequestMethod.GET)
    @RequiresPermissions("over")
    public void searchBuilding(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            List<JSONObject> buildings = adminService.searchBuilding();
            writer.write(buildings.toString());
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("一条信息也没有哦！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 根据用户基本信息查询匹配的用户
     *
     * @param response
     * @param param
     */
    @RequestMapping(value = "/informationSearch/studentBasicInfo", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void informationSearch(HttpServletResponse response, @RequestBody JSONObject param) {
        int pageNow = param.getIntValue("pageNow");
        Integer userNumber = 0;
        int pageSize = 8;
        int pages = 0;
        List<JSONObject> users = null;
        PrintWriter writer = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            writer = response.getWriter();
            userNumber = adminService.countUserNumberByCondition(param);
            param.put("pageSize", pageSize);
            if (userNumber % pageSize == 0) {
                pages = userNumber / pageSize;
            } else {
                pages = userNumber / pageSize + 1;
            }
            //查询首页数据
            if (pageNow == 0) {
                param.put("startRow", 0);
                users = adminService.listUserByCondition(param);
            } else {
                //查询特定页数据
                param.put("startRow", (pageNow - 1) * pageSize);
                users = adminService.listUserByCondition(param);
            }
            users.get(0).put("pages", pages);
            writer.write(users.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            writer.write("一个用户也没有哦！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 统计楼栋居住情况
     *
     * @param response
     */
    @RequestMapping(value = "/informationSearch/analysis", method = RequestMethod.GET)
    @RequiresPermissions("over")
    public void analysis(HttpServletResponse response) {
        List<List<List<JSONObject>>> allBuildingsCategory = null;
        List<List<JSONObject>> allBuildings = null;
        List<JSONObject> allRooms = null;
        PrintWriter writer = null;
        //redis
        ValueOperations<String, List<List<List<JSONObject>>>> operations = redisTemplate.opsForValue();
        try {
            response.setContentType("text/html;charset=utf-8");
            writer = response.getWriter();
            //使用缓存
            if (redisTemplate.hasKey("buildingsCategory")) {
                allBuildingsCategory = operations.get("buildingsCategory");
            } else {
                List<JSONObject> users = adminService.analysisUser();
                allBuildings = new ArrayList<>();
                while (users.size() > 0) {
                    JSONObject tempUser = users.get(0);
                    allRooms = new ArrayList<>();
                    allRooms.add(tempUser);
                    for (int i = 1; i < users.size(); ) {
                        if (tempUser.getString("studentBuildingName").equals(users.get(i).getString("studentBuildingName")) &&
                                tempUser.getString("studentRoomName").equals(users.get(i).getString("studentRoomName"))) {
                            allRooms.add(users.get(i));
                            users.remove(i);
                        } else if (i < users.size()) {
                            i++;
                        }
                    }
                    users.remove(0);
                    allBuildings.add(allRooms);
                }

                allBuildingsCategory = new ArrayList<>();
                List<List<JSONObject>> oneBuilding = null;
                while (allBuildings.size() > 0) {
                    oneBuilding = new ArrayList<>();
                    oneBuilding.add(allBuildings.get(0));
                    for (int j = 1; j < allBuildings.size(); ) {
                        if (allBuildings.get(j).get(0).getString("studentBuildingName").equals(
                                allBuildings.get(0).get(0).getString("studentBuildingName"))) {
                            oneBuilding.add(allBuildings.get(j));
                            allBuildings.remove(j);
                        } else if (j < allBuildings.size()) {
                            j++;
                        }
                    }
                    allBuildings.remove(0);
                    allBuildingsCategory.add(oneBuilding);
                }
                //缓存
                operations.set("buildingsCategory", allBuildingsCategory);
            }
            writer.write(allBuildingsCategory.toString());
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("一条信息也没有哦！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 列举进出楼物品
     *
     * @param response
     */
    @RequestMapping(value = "/inOutRegister/list", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void listInOutRegister(HttpServletResponse response, @RequestBody JSONObject param) {
        int pageNow = param.getIntValue("pageNow");
        Integer inOutRegisterNumber = 0;
        int pageSize = 8;
        int pages = 0;
        List<JSONObject> inOutRegisters = null;
        PrintWriter writer = null;
        response.setContentType("text/html;charset=utf-8");
        try {
            writer = response.getWriter();
            inOutRegisterNumber = adminService.countInOutRegisterNumber();
            if (inOutRegisterNumber % pageSize == 0) {
                pages = inOutRegisterNumber / pageSize;
            } else {
                pages = inOutRegisterNumber / pageSize + 1;
            }
            //查询首页数据
            if (pageNow == 0) {
                inOutRegisters = adminService.listInOutRegister(0, pageSize);
            } else {
                //查询特定页数据
                inOutRegisters = adminService.listInOutRegister((pageNow-1)*pageSize, pageSize);
            }
            inOutRegisters.get(0).put("pages", pages);
            writer.write(inOutRegisters.toString());
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("一条记录也没有哦！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    @RequestMapping(value = "/inOutRegister/update", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void updateInOutRegister(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.updateInOutRegister(param);
            writer.write("更新成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("更新失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 删除
     *
     * @param response
     */
    @RequestMapping(value = "/inOutRegister/delete", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void deleteInOutRegister(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.deleteInOutRegister(param);
            writer.write("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("删除失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }


    /**
     * 添加进/出楼物品登记
     *
     * @param response
     * @param param
     */
    @RequestMapping(value = "/inOutRegister/add", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void inOutRegisterIn(HttpServletResponse response, @RequestBody List<JSONObject> param) {
        PrintWriter writer = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            writer = response.getWriter();
            for(JSONObject temp : param){
                temp.put("registerTime",new Date());
            }
            adminService.addInOutRegister(param);
            writer.write("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("添加失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 列举人员访问记录
     *
     * @param response
     */
    @RequestMapping(value = "/inOutRegister/mockSurvey/list", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void listMockSurvey(HttpServletResponse response, @RequestBody JSONObject param) {
        int pageNow = param.getIntValue("pageNow");
        Integer MockSurveyNumber = 0;
        int pageSize = 8;
        int pages = 0;
        List<JSONObject> MockSurveys = null;
        PrintWriter writer = null;
        response.setContentType("text/html;charset=utf-8");
        try {
            writer = response.getWriter();
            MockSurveyNumber = adminService.countMockSurveyNumber(param);
            if (MockSurveyNumber % pageSize == 0) {
                pages = MockSurveyNumber / pageSize;
            } else {
                pages = MockSurveyNumber / pageSize + 1;
            }
            param.put("pageSize",pageSize);
            //查询首页数据
            if (pageNow == 0) {
                param.put("startRow",0);
                MockSurveys = adminService.listMockSurvey(param);
            } else {
                //查询特定页数据
                param.put("startRow",(pageNow-1)*pageSize);
                MockSurveys = adminService.listMockSurvey(param);
            }
            MockSurveys.get(0).put("pages", pages);
            writer.write(MockSurveys.toString());
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("一条记录也没有哦！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    @RequestMapping(value = "/inOutRegister/mockSurvey/update", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void updateMockSurvey(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.updateMockSurvey(param);
            writer.write("更新成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("更新失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    @RequestMapping(value = "/inOutRegister/mockSurvey/concludeVisit", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void mockSurveyConcludeVisit(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            param.put("visitorOut",new Date());
            adminService.mockSurveyConcludeVisit(param);
            writer.write("操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("操作失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 删除
     *
     * @param response
     */
    @RequestMapping(value = "/inOutRegister/mockSurvey/delete", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void deleteMockSurvey(HttpServletResponse response, @RequestBody JSONObject param) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            adminService.deleteMockSurvey(param);
            writer.write("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("删除失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }


    /**
     * 添加人员来访登记
     *
     * @param response
     * @param param
     */
    @RequestMapping(value = "/inOutRegister/mockSurvey/add", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void mockSurveyAdd(HttpServletResponse response, @RequestBody List<JSONObject> param) {
        PrintWriter writer = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            writer = response.getWriter();
            for(JSONObject temp : param){
                temp.put("visitorIn",new Date());
            }
            adminService.addMockSurvey(param);
            writer.write("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("添加失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }


    /**
     * 查询通知
     *
     * @return
     */
    @RequiresPermissions("over")
    @RequestMapping(value = "/user/houseparent/timeSearch", method = RequestMethod.POST)
    public void houseparentTimeSearch(@RequestBody JSONObject param, HttpServletResponse response) {
        Date startTime = param.getDate("startTime");
        Date endTime = param.getDate("endTime");
        int page = param.getIntValue("page");
        int pageSize = 8;
        Integer notificationNumber = null;
        List<JSONObject> lastNotifications = null;
        if (startTime == null || endTime == null) {
            notificationNumber = adminService.countNotification();
            if (page == 0) {
                lastNotifications = adminService.listLastNotification(pageSize);
            } else {
                lastNotifications = adminService.listLastNotification1(pageSize, page);
            }
        } else {
            notificationNumber = adminService.countNotification1(startTime, endTime);
            if (page == 0) {
                lastNotifications = adminService.listLastNotification2(startTime, endTime, pageSize);
            } else {
                lastNotifications = adminService.listLastNotification3(startTime, endTime, pageSize, page);
            }
        }
        int pages = 0;
        if (notificationNumber % pageSize == 0) {
            pages = notificationNumber / pageSize;
        } else {
            pages = notificationNumber / pageSize + 1;
        }
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        if (lastNotifications == null || lastNotifications.isEmpty()) {
            try {
                writer = response.getWriter();
                writer.write("一条通知也没有！");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                writer.flush();
                writer.close();
            }
        } else {
            lastNotifications.get(0).put("pages", pages);
            try {
                writer = response.getWriter();
                writer.write(lastNotifications.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                writer.flush();
                writer.close();
            }
        }
    }

    /**
     * 获取该用户可以换的宿舍
     *
     * @return
     */
    @RequiresPermissions("over")
    @RequestMapping(value = "/user/houseparent/changeOfDormitoryApply", method = RequestMethod.GET)
    public void getChangeOfDormitoryApply(HttpServletResponse response) {
        Subject currentUser = SecurityUtils.getSubject();
        String userName = (String) currentUser.getPrincipal();
        String className = userName.substring(0, 9);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            List<List<JSONObject>> rooms = adminService.getChangeOfDormitoryApply(className);
            writer.write(rooms.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }


    /**
     * 提交用户换的宿舍
     *
     * @return
     */
    @RequiresPermissions("over")
    @RequestMapping(value = "/user/houseparent/changeOfDormitoryApply", method = RequestMethod.POST)
    public void ChangeOfDormitoryApply(@RequestBody JSONObject param, HttpServletResponse response) {
        Subject currentUser = SecurityUtils.getSubject();
        String userName = (String) currentUser.getPrincipal();
        param.put("studentId", userName);
        param.put("submitTime", new Date());
        param.put("applicationContent", param.getString("applicationContent") + param.getString("roomNumber"));
        PrintWriter writer = null;
        response.setContentType("text/html;charset=utf-8");
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            logger.info(e.toString());
        }
        try {
//            adminService.updateUserRoomNumber(userName, roomNumber);
            adminService.submitApplication(param);
            writer.write("更换宿舍申请成功！");
        } catch (Exception e) {
            logger.info(e.toString());
            writer.write("更换宿舍申请失败！");
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * 保存室外报修申请
     *
     * @param response
     */
    @RequestMapping(value = "/user/houseparent/outdoorRepair", method = RequestMethod.POST)
    @RequiresPermissions("over")
    public void outdoorRepair(HttpServletResponse response, @RequestBody List<JSONObject> params) {
        //获取提交申请的用户
        Subject currentUser = SecurityUtils.getSubject();
        String userName = (String) currentUser.getPrincipal();
    }
}
