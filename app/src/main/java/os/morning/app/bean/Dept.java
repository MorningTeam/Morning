package os.morning.app.bean;

/**
 * 部门信息
 * @author  ZiQin
 * @version 1.0
 * @created 2014/7/21
 */
public class Dept extends BaseEntity{

    private String deptId;
    private String deptName;

    public void setDeptId(String deptId){
        this.deptId = deptId;
    }
    public String getDeptId(){
        return this.deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptName() {
        return deptName;
    }
    public Dept(String deptId,String deptName){
        this.setDeptId(deptId);
        this.setDeptName(deptName);
    }
}
