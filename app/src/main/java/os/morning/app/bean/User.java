package os.morning.app.bean;

/**
 * 用户个人信息
 * @author  ZiQin
 * @version 1.0
 * @created 2014/7/21
 */
public class User extends BaseEntity{

    private String userNo;
    private String userName;
    private String email;
    private String mobile;
    private String deptId;
    private String deptName;

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptId() {
        return deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserNo() {
        return userNo;
    }
}
