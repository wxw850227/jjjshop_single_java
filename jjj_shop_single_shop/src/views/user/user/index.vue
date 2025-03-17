<template>
  <div class="user">
    <!--搜索表单-->
    <div class="common-seach-wrap">
      <el-form size="small" :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="昵称/手机号">
          <el-input v-model="formInline.nickName" placeholder="请输入昵称/手机号"></el-input>
        </el-form-item>
        <el-form-item label="会员等级">
          <el-select v-model="formInline.gradeId" placeholder="请选择">
            <el-option label="全部" value="0"></el-option>
            <el-option v-for="(item, index) in gradeList" :key="index" :label="item.name" :value="item.gradeId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="注册时间">
          <div class="block">
            <span class="demonstration"></span>
            <el-date-picker v-model="formInline.startDate" type="date" value-format="YYYY-MM-DD" placeholder="选择开始日期">
            </el-date-picker>
            <span>-</span>
            <el-date-picker v-model="formInline.endDate" type="date" value-format="YYYY-MM-DD" placeholder="选择结束日期">
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <div class="mb16">
        <span>标签</span>
        <span class="pl10">
          <el-radio-group v-model="formInline.tagId" size="small" @change="onSubmit">
            <el-radio-button label="0">全部</el-radio-button>
            <el-radio-button :label="item.tagId" v-for="(item, index) in allTag" :key="index" :value="item.tagId">
              {{item.tagName}}
            </el-radio-button>
          </el-radio-group>
        </span>
      </div>
    </div>
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
          <el-table-column prop="userId" label="ID" width="80"></el-table-column>
          <el-table-column prop="nickname" label="昵称"></el-table-column>
          <el-table-column label="微信头像">
            <template #default="scope">
              <img :src="scope.row.avatarurl" :width="30" :height="30" />
            </template>
          </el-table-column>
          <el-table-column prop="gradeName" label="会员等级"></el-table-column>
          <el-table-column prop="balance" label="余额"></el-table-column>
          <el-table-column prop="mobile" label="手机号"></el-table-column>
          <el-table-column prop="createTime" label="注册时间" width="140"></el-table-column>
          <el-table-column fixed="right" label="操作" width="190">
            <template #default="scope">
              <el-button @click="addClick(scope.row)" type="text" size="small" v-auth="'/user/user/recharge'">充值
              </el-button>
              <el-button @click="editClick(scope.row)" type="text" size="small" v-auth="'/user/user/edit'">等级
              </el-button>
              <el-button @click="tagClick(scope.row)" type="text" size="small" v-auth="'/user/user/delete'">标签
              </el-button>
              <el-button @click="deleteClick(scope.row)" type="text" size="small" v-auth="'/user/user/delete'">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!--分页-->
      <div class="pagination">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background
          :current-page="curPage" :page-size="pageSize" layout="total, prev, pager, next, jumper"
          :total="totalDataNumber"></el-pagination>
      </div>
    </div>

    <!--会员充值-->
    <Recharge v-if="open_add" :open_add="open_add" :form="userModel" :gradeList="gradeList"
      @closeDialog="closeDialogFunc($event, 'add')"></Recharge>
    <!--会员等级-->
    <Grade v-if="open_edit" :open_edit="open_edit" :form="userModel" :gradeList="gradeList"
      @closeDialog="closeDialogFunc($event, 'edit')"></Grade>
    <!--会员标签-->
    <Tag v-if="open_tag" :open_tag="open_tag" :form="userModel" @closeDialog="closeDialogFunc($event, 'tag')"></Tag>
  </div>
</template>

<script>
  import UserApi from '@/api/user.js';
  import Grade from './dialog/Grade.vue';
  import Recharge from './dialog/Recharge.vue';
  import Tag from './dialog/Tag.vue';
  export default {
    components: {
      /*编辑组件*/
      Grade,
      Recharge,
      Tag
    },
    data () {
      return {
        /*是否加载完成*/
        loading: true,
        /*列表数据*/
        tableData: [],
        /*一页多少条*/
        pageSize: 20,
        /*一共多少条数据*/
        totalDataNumber: 0,
        /*当前是第几页*/
        curPage: 1,
        /*横向表单数据模型*/
        formInline: {
          nickName: '',
          gradeId: '',
          tagId: 0,
        },
        /*是否打开添加弹窗*/
        open_add: false,
        /*是否打开编辑弹窗*/
        open_edit: false,
        /*当前编辑的对象*/
        userModel: {},
        /*等级*/
        gradeList: [],
        open_tag: false,
        /*所有标签*/
        allTag: [],
        pickerOptions: {
          disabledDate (time) {
            return time.getTime() < Date.now() - 8.64e7;
          },
        },
      };
    },
    created () {
      /*获取列表*/
      this.getTableList();
    },
    methods: {
      /*选择第几页*/
      handleCurrentChange (val) {
        let self = this;
        self.curPage = val;
        self.loading = true;
        self.getTableList();
      },

      /*每页多少条*/
      handleSizeChange (val) {
        this.curPage = 1;
        this.pageSize = val;
        this.getTableList();
      },

      /*获取列表*/
      getTableList () {
        let self = this;
        let Params = self.formInline;
        Params.pageIndex = self.curPage;
        Params.pageSize = self.pageSize;
        UserApi.userlist(Params, true)
          .then(res => {
            self.loading = false;
            self.tableData = res.data.userList.records;
            self.totalDataNumber = res.data.userList.total;
            self.gradeList = res.data.gradeList;
            self.allTag = res.data.tagList;
          })
          .catch(error => {
            self.loading = false;
          });
      },

      /*搜索查询*/
      onSubmit () {
        let self = this;
        self.loading = true;
        self.curPage = 1;
        self.getTableList();
      },

      /*打开添加*/
      addClick (item) {
        this.userModel = item;
        this.open_add = true;
      },

      /*打开编辑*/
      editClick (item) {
        this.userModel = item;
        this.open_edit = true;
      },

      tagClick (item) {
        this.userModel = item;
        this.open_tag = true;
      },

      /*关闭弹窗*/
      closeDialogFunc (e, f) {
        if (f == 'add') {
          this.open_add = e.openDialog;
          if (e.type == 'success') {
            this.getTableList();
          }
        }
        if (f == 'edit') {
          this.open_edit = e.openDialog;
          if (e.type == 'success') {
            this.getTableList();
          }
        }
        if (f == 'tag') {
          this.open_tag = e.openDialog;
          if (e.type == 'success') {
            this.getTableList();
          }
        }
      },

      /*删除用户*/
      deleteClick (row) {
        let self = this;
        ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then(() => {
            self.loading = true;
            UserApi.deleteuser(
              {
                userId: row.userId
              },
              true
            )
              .then(data => {
                self.loading = false;
                if (data.code == 1) {
                  ElMessage ({
                    message: '恭喜你，用户删除成功',
                    type: 'success'
                  });
                  self.getTableList();
                } else {
                  self.loading = false;
                }
              })
              .catch(error => {
                self.loading = false;
              });
          })
          .catch(() => {
            self.loading = false;
          });
      }
    }
  };
</script>
<style></style>
