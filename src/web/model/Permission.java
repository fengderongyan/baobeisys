package web.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sgy.util.db.DBHelperSpring;
import com.sgy.util.common.StringHelper;

public class Permission implements Serializable {
    private StringHelper str = new StringHelper();

    private DBHelperSpring db;

    private HashMap permissionMap = new HashMap();

    public DBHelperSpring getDb() {
        return db;
    }

    public void setDb(DBHelperSpring db) {
        this.db = db;
    }

    public void loadPermissions() {
        HashMap newPermissionMap = new HashMap();
        
        // 加载t_module中所有菜单结点
        String sql = "select module_id,module_url from t_module ";
        List list = db.queryForList(sql);
        String tmpRoleId = "";
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String module_id = str.notEmpty(map.get("module_id"));
            String module_url = str.notEmpty(map.get("module_url"));
            sql = "select role_id from t_role_module where module_id = ? ";
            List roleList = db.queryForList(sql, new Object[] {module_id });
            String role_ids = "";
            if (roleList.size() > 0) {
                Map tmpMap;
                for (int j = 0; j < roleList.size() - 1; j++) {
                    tmpMap = (Map) roleList.get(j);
                    tmpRoleId = str.notEmpty(tmpMap.get("role_id"));
                    role_ids += tmpRoleId + ",";
                }
                tmpMap = (Map) roleList.get(roleList.size() - 1);
                tmpRoleId = str.notEmpty(tmpMap.get("role_id"));
                role_ids += tmpRoleId;
            }
            newPermissionMap.put(module_url, role_ids);
        }
        this.setPermissionMap(newPermissionMap);
    }

    public HashMap getPermissionMap() {
        return permissionMap;
    }

    public void setPermissionMap(HashMap permissionMap) {
        this.permissionMap = permissionMap;
    }
}
