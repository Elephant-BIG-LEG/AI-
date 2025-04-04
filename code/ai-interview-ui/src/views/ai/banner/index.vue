<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true" label-width="68px">
            <el-form-item label="标题" prop="title">
                <el-input
                    v-model="queryParams.title"
                    placeholder="请输入Banner名称"
                    clearable
                    style="width: 240px"
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    icon="Plus"
                    @click="handleAdd"
                    v-hasPermi="['system:role:add']"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="success"
                    plain
                    icon="Edit"
                    :disabled="single"
                    @click="handleUpdate"
                    v-hasPermi="['system:role:edit']"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="danger"
                    plain
                    icon="Delete"
                    :disabled="multiple"
                    @click="handleDelete"
                    v-hasPermi="['system:role:remove']"
                >删除</el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 表格数据 -->
        <el-table v-loading="loading" :data="bannerList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="Banner编号" prop="id" width="120" />
            <el-table-column label="Banner名称" prop="title" :show-overflow-tooltip="true" width="150" />
            <el-table-column label="连接地址" prop="url" :show-overflow-tooltip="true" width="150" />
            <el-table-column label="图片" width="100">
                <template #default="scope">
<!--                    <img :src="scope.row.image" style="width: 72px;height: 48px" alt="">-->
                    <img :src="'https://pic4.zhimg.com/v2-c34c61a90095abb8713de9d1dca7ec7b_r.jpg'" style="width: 72px;height: 48px" alt="">
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-tooltip content="修改" placement="top" >
                        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['ai:banner:update']"></el-button>
                    </el-tooltip>
                    <el-tooltip content="删除" placement="top" >
                        <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['ai:banner:remove']"></el-button>
                    </el-tooltip>
                </template>
            </el-table-column>
        </el-table>

        <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
        />

        <!-- 添加或修改角色配置对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="roleRef" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="Banner名称" prop="title">
                    <el-input v-model="form.title" placeholder="请输入Banner名称" />
                </el-form-item>
                <el-form-item label="Banner连接" prop="url">
                    <el-input v-model="form.url" placeholder="请输入Banner连接" />
                </el-form-item>
                <el-form-item label="Banner图片" prop="url">
                    <el-upload
                        action="http://192.168.200.1:8080/file/upload"
                        :on-success="fileUploadSuccess"
                        :limit="1"
                        >
                        <el-button size="small" type="primary">上传图片</el-button>
                        <img :src="form.image" v-if="form.image" alt="">
                    </el-upload>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 分配角色数据权限对话框 -->
        <el-dialog :title="title" v-model="openDataScope" width="500px" append-to-body>
            <el-form :model="form" label-width="80px">
                <el-form-item label="角色名称">
                    <el-input v-model="form.title" :disabled="true" />
                </el-form-item>
                <el-form-item label="权限字符">
                    <el-input v-model="form.roleKey" :disabled="true" />
                </el-form-item>
                <el-form-item label="权限范围">
                    <el-select v-model="form.dataScope" @change="dataScopeSelectChange">
                        <el-option
                            v-for="item in dataScopeOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="数据权限" v-show="form.dataScope == 2">
                    <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand($event, 'dept')">展开/折叠</el-checkbox>
                    <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event, 'dept')">全选/全不选</el-checkbox>
                    <el-checkbox v-model="form.deptCheckStrictly" @change="handleCheckedTreeConnect($event, 'dept')">父子联动</el-checkbox>
                    <el-tree
                        class="tree-border"
                        :data="deptOptions"
                        show-checkbox
                        default-expand-all
                        ref="deptRef"
                        node-key="id"
                        :check-strictly="!form.deptCheckStrictly"
                        empty-text="加载中，请稍候"
                        :props="{ label: 'label', children: 'children' }"
                    ></el-tree>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitDataScope">确 定</el-button>
                    <el-button @click="cancelDataScope">取 消</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="Role">
import { addRole, changeRoleStatus, dataScope, delRole, getRole, listRole, updateRole, deptTreeSelect } from "@/api/system/role";
import { roleMenuTreeselect, treeselect as menuTreeselect } from "@/api/system/menu";
import {listBanner} from "../../../api/ai/banner.js";
const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const bannerList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const menuOptions = ref([]);
const menuExpand = ref(false);
const menuNodeAll = ref(false);
const deptExpand = ref(true);
const deptNodeAll = ref(false);
const deptOptions = ref([]);
const openDataScope = ref(false);
const menuRef = ref(null);
const deptRef = ref(null);

/** 数据范围选项*/
const dataScopeOptions = ref([
    { value: "1", label: "全部数据权限" },
    { value: "2", label: "自定数据权限" },
    { value: "3", label: "本部门数据权限" },
    { value: "4", label: "本部门及以下数据权限" },
    { value: "5", label: "仅本人数据权限" }
]);

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        roleKey: undefined,
        status: undefined
    },
    rules: {
        title: [{ required: true, message: "Banner名称不能为空", trigger: "blur" }],
        url: [{ required: true, message: "连接不能为空", trigger: "blur" }]
    },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询角色列表 */
function getList() {
    loading.value = true;
    listBanner(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
        bannerList.value = response.rows;
        total.value = response.total;
        loading.value = false;
    });
}

/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}
function fileUploadSuccess (res) {
    console.log(res)
}
/** 重置按钮操作 */
function resetQuery() {
    dateRange.value = [];
    proxy.resetForm("queryRef");
    handleQuery();
}

/** 删除按钮操作 */
function handleDelete(row) {
    const roleIds = row.roleId || ids.value;
    proxy.$modal.confirm('是否确认删除角色编号为"' + roleIds + '"的数据项?').then(function () {
        return delRole(roleIds);
    }).then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
    proxy.download("system/role/export", {
        ...queryParams.value,
    }, `role_${new Date().getTime()}.xlsx`);
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.roleId);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
}

/** 角色状态修改 */
function handleStatusChange(row) {
    let text = row.status === "0" ? "启用" : "停用";
    proxy.$modal.confirm('确认要"' + text + '""' + row.title + '"角色吗?').then(function () {
        return changeRoleStatus(row.roleId, row.status);
    }).then(() => {
        proxy.$modal.msgSuccess(text + "成功");
    }).catch(function () {
        row.status = row.status === "0" ? "1" : "0";
    });
}

/** 更多操作 */
function handleCommand(command, row) {
    switch (command) {
        case "handleDataScope":
            handleDataScope(row);
            break;
        case "handleAuthUser":
            handleAuthUser(row);
            break;
        default:
            break;
    }
}

/** 分配用户 */
function handleAuthUser(row) {
    router.push("/system/role-auth/user/" + row.roleId);
}

/** 查询菜单树结构 */
function getMenuTreeselect() {
    menuTreeselect().then(response => {
        menuOptions.value = response.data;
    });
}

/** 所有部门节点数据 */
function getDeptAllCheckedKeys() {
    // 目前被选中的部门节点
    let checkedKeys = deptRef.value.getCheckedKeys();
    // 半选中的部门节点
    let halfCheckedKeys = deptRef.value.getHalfCheckedKeys();
    checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
    return checkedKeys;
}

/** 重置新增的表单以及其他数据  */
function reset() {
    if (menuRef.value != undefined) {
        menuRef.value.setCheckedKeys([]);
    }
    menuExpand.value = false;
    menuNodeAll.value = false;
    deptExpand.value = true;
    deptNodeAll.value = false;
    form.value = {
        roleId: undefined,
        title: undefined,
        roleKey: undefined,
        roleSort: 0,
        status: "0",
        menuIds: [],
        deptIds: [],
        menuCheckStrictly: true,
        deptCheckStrictly: true,
        remark: undefined
    };
    proxy.resetForm("roleRef");
}

/** 添加角色 */
function handleAdd() {
    reset();
    getMenuTreeselect();
    open.value = true;
    title.value = "添加角色";
}

/** 修改角色 */
function handleUpdate(row) {
    reset();
    const roleId = row.roleId || ids.value;
    const roleMenu = getRoleMenuTreeselect(roleId);
    getRole(roleId).then(response => {
        form.value = response.data;
        form.value.roleSort = Number(form.value.roleSort);
        open.value = true;
        nextTick(() => {
            roleMenu.then((res) => {
                let checkedKeys = res.checkedKeys;
                checkedKeys.forEach((v) => {
                    nextTick(() => {
                        menuRef.value.setChecked(v, true, false);
                    });
                });
            });
        });
    });
    title.value = "修改角色";
}

/** 根据角色ID查询菜单树结构 */
function getRoleMenuTreeselect(roleId) {
    return roleMenuTreeselect(roleId).then(response => {
        menuOptions.value = response.menus;
        return response;
    });
}

/** 根据角色ID查询部门树结构 */
function getDeptTree(roleId) {
    return deptTreeSelect(roleId).then(response => {
        deptOptions.value = response.depts;
        return response;
    });
}

/** 树权限（展开/折叠）*/
function handleCheckedTreeExpand(value, type) {
    if (type == "menu") {
        let treeList = menuOptions.value;
        for (let i = 0; i < treeList.length; i++) {
            menuRef.value.store.nodesMap[treeList[i].id].expanded = value;
        }
    } else if (type == "dept") {
        let treeList = deptOptions.value;
        for (let i = 0; i < treeList.length; i++) {
            deptRef.value.store.nodesMap[treeList[i].id].expanded = value;
        }
    }
}

/** 树权限（全选/全不选） */
function handleCheckedTreeNodeAll(value, type) {
    if (type == "menu") {
        menuRef.value.setCheckedNodes(value ? menuOptions.value : []);
    } else if (type == "dept") {
        deptRef.value.setCheckedNodes(value ? deptOptions.value : []);
    }
}

/** 树权限（父子联动） */
function handleCheckedTreeConnect(value, type) {
    if (type == "menu") {
        form.value.menuCheckStrictly = value ? true : false;
    } else if (type == "dept") {
        form.value.deptCheckStrictly = value ? true : false;
    }
}

/** 所有菜单节点数据 */
function getMenuAllCheckedKeys() {
    // 目前被选中的菜单节点
    let checkedKeys = menuRef.value.getCheckedKeys();
    // 半选中的菜单节点
    let halfCheckedKeys = menuRef.value.getHalfCheckedKeys();
    checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
    return checkedKeys;
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs["roleRef"].validate(valid => {
        if (valid) {
            if (form.value.roleId != undefined) {
                form.value.menuIds = getMenuAllCheckedKeys();
                updateRole(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功");
                    open.value = false;
                    getList();
                });
            } else {
                form.value.menuIds = getMenuAllCheckedKeys();
                addRole(form.value).then(response => {
                    proxy.$modal.msgSuccess("新增成功");
                    open.value = false;
                    getList();
                });
            }
        }
    });
}

/** 取消按钮 */
function cancel() {
    open.value = false;
    reset();
}

/** 选择角色权限范围触发 */
function dataScopeSelectChange(value) {
    if (value !== "2") {
        deptRef.value.setCheckedKeys([]);
    }
}

/** 分配数据权限操作 */
function handleDataScope(row) {
    reset();
    const deptTreeSelect = getDeptTree(row.roleId);
    getRole(row.roleId).then(response => {
        form.value = response.data;
        openDataScope.value = true;
        nextTick(() => {
            deptTreeSelect.then(res => {
                nextTick(() => {
                    if (deptRef.value) {
                        deptRef.value.setCheckedKeys(res.checkedKeys);
                    }
                });
            });
        });
    });
    title.value = "分配数据权限";
}

/** 提交按钮（数据权限） */
function submitDataScope() {
    if (form.value.roleId != undefined) {
        form.value.deptIds = getDeptAllCheckedKeys();
        dataScope(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            openDataScope.value = false;
            getList();
        });
    }
}

/** 取消按钮（数据权限）*/
function cancelDataScope() {
    openDataScope.value = false;
    reset();
}

getList();
</script>
