package com.chenxin.houseparent.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HtmlController {

    private static final Logger logger = LoggerFactory.getLogger(HtmlController.class);

    @RequestMapping(value = "/admin/index", method = RequestMethod.GET)
    @RequiresPermissions("over")
    public String index() {
        return "admin/frameset";
    }

    /**
     * 跳转到修改密码页面
     *
     * @return
     */
    @RequestMapping(value = "/admin/forgetPassWord", method = RequestMethod.GET)
    public String forgetPassWord() {
        return "admin/updatePassword";
    }

    /**
     * 管理员再登陆
     *
     * @return
     */
    @RequestMapping(value = "/admin/reLogin", method = RequestMethod.GET)
    public String reLogin() {
        return "admin/login";
    }


    @RequiresPermissions("over")
    @RequestMapping(value = "/admin/left", method = RequestMethod.GET)
    public String left() {
        return "admin/left";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/admin/top", method = RequestMethod.GET)
    public String top() {
        return "admin/top";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/systemManage/addUser", method = RequestMethod.GET)
    public String timeSearch() {
        return "systemManage/addUser";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/apartmentManage/building", method = RequestMethod.GET)
    public String building() {
        return "apartmentManage/building";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/apartmentManage/addBuilding", method = RequestMethod.GET)
    public String addBuilding() {
        return "apartmentManage/addBuilding";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/apartmentManage/addRoom", method = RequestMethod.GET)
    public String addRoom() {
        return "apartmentManage/addRoom";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/apartmentManage/employee", method = RequestMethod.GET)
    public String employee() {
        return "apartmentManage/employee";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/apartmentManage/employee/addEmployee", method = RequestMethod.GET)
    public String addEmployee() {
        return "apartmentManage/addEmployee";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/user/houseparent/indoorRepair", method = RequestMethod.GET)
    public String indoorRepair() {
        return "user/houseparent/indoorRepair";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/systemManage/userManage", method = RequestMethod.GET)
    public String changeRoomApply() {
        return "systemManage/userManage";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/apartmentManage/room", method = RequestMethod.GET)
    public String outdoorRepair() {
        return "apartmentManage/room";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/informationSearch/studentBasicInfo", method = RequestMethod.GET)
    public String studentIdSearch() {
        return "informationSearch/studentBasicInfo";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/informationSearch/buildingCondition", method = RequestMethod.GET)
    public String buildingCondition() {
        return "informationSearch/buildingCondition";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/inOutRegister/inOutRegister", method = RequestMethod.GET)
    public String inOutRegister() {
        return "inOutRegister/inOutRegister";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/inOutRegister/in", method = RequestMethod.GET)
    public String inOutRegisterIn() {
        return "inOutRegister/in";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/inOutRegister/out", method = RequestMethod.GET)
    public String inOutRegisterOut() {
        return "inOutRegister/out";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/inOutRegister/mockSurvey", method = RequestMethod.GET)
    public String inOutRegisterMockSurvey() {
        return "inOutRegister/mockSurvey";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/inOutRegister/addMockSurvey", method = RequestMethod.GET)
    public String addMockSurvey() {
        return "inOutRegister/addMockSurvey";
    }

    @RequiresPermissions("over")
    @RequestMapping(value = "/inOutRegister/concludeVisit", method = RequestMethod.GET)
    public String concludeVisit() {
        return "inOutRegister/concludeVisit";
    }

    /**
     * 未授权页面
     */
    @RequestMapping(value = "/admin/403", method = RequestMethod.GET)
    public String _403() {
        return "403";
    }
}
