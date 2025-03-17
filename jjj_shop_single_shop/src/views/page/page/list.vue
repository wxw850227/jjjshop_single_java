<template>
  <div class="d-b-s diy-preview">
    <!--手机diy容器-->
    <div class="diy-phone"><Model v-if="!loading" ref="model" :form="form" :defaultData="defaultData" :diyData="diyData"></Model></div>
    <div class="user flex-1">
      <!--添加页面-->
      <div class="common-level-rail">
        <el-button size="small" type="primary" icon="Plus" @click="addClick()" v-auth="'/page/page/addPage'">添加页面</el-button>
      </div>

      <!--内容-->
      <div class="product-content">
        <div class="table-wrap">
          <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="pageId" label="页面ID" width="80"></el-table-column>
            <el-table-column prop="pageName" label="页面名称">
              <template #default="scope">
                <span>{{ scope.row.pageName }}</span>
                <span class="red" v-if="scope.row.isDefault">(默认)</span>
              </template>
            </el-table-column>
            <el-table-column prop="pageType" label="页面类型">
              <template #default="scope">
                <span v-if="scope.row.pageType==10">商城首页</span>
                <span v-if="scope.row.pageType==20">自定义页</span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="添加时间"></el-table-column>
            <el-table-column prop="updateTime" label="更新时间"></el-table-column>
            <el-table-column fixed="right" label="操作" width="150">
              <template #default="scope">
                <el-button v-if="!scope.row.isDefault" @click="setHomeClick(scope.row.pageId)" type="text" size="small" v-auth="'/page/page/setPage'">设为默认</el-button>
                <el-button @click="editClick(scope.row.pageId)" type="text" size="small" v-auth="'/page/page/editPage'">编辑</el-button>
                <el-button  @click="deleteClick(scope.row.pageId)" type="text" size="small" v-auth="'/page/page/deletePage'">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!--分页-->
        <div class="pagination">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background :current-page="curPage"
            :page-size="pageSize" layout="total, prev, pager, next, jumper"
            :total="totalDataNumber">
          </el-pagination>
        </div>
      </div>

    </div>
  </div>

</template>

<script>
  import PageApi from '@/api/page.js';
  import Model from './diy/Preview.vue';
  export default {
    components: {
      Model,
    },
    data() {
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
        /*默认数据*/
        defaultData: {},
        /*组件数据列表*/
        diyData: {
          items: []
        },
        /*表单对象*/
        form: {
          umeditor: {},
          /*当前选中*/
          curItem: {},
          /*当前选中的元素（下标）*/
          selectedIndex: -1
        }
      };
    },
    created() {
      /*获取列表*/
      this.getTableList();

    },
    methods: {
      onEditer(){
        return
      },
      /*选择第几页*/
      handleCurrentChange(val) {
        let self = this;
        self.curPage = val;
        self.loading = true;
        self.getTableList();
      },

      /*每页多少条*/
      handleSizeChange(val) {
        this.curPage = 1;
        this.pageSize = val;
        this.getTableList();
      },

      /*获取列表*/
      getTableList() {
        let self = this;
        let Params = {};
        Params.page = self.curPage;
        Params.list_rows = self.pageSize;
        PageApi.HomeList(Params, true).then(res => {
          self.loading = false;
          self.tableData = res.data.list.records;
          self.diyData = res.data.default.pageDataJson;
          self.totalDataNumber = res.data.list.total;
        }).catch(error => {

        });
      },

      /*删除数据*/
      deleteClick(row) {
        let self = this;
        ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          self.loading = true;
          PageApi.deletePage({
              pageId: row
            }, true).then(data => {
              self.loading = false;
              ElMessage ({
                message: '恭喜你，删除成功',
                type: 'success'
              });
              self.getTableList();
            })
            .catch(error => {
              self.loading = false;
            });

        }).catch(() => {});
      },

      /*添加页面*/
      addClick() {
        this.$router.push('/page/page/addPage');
      },

      /*设为首页*/
      setHomeClick(pageId) {
        let self = this;
        ElMessageBox.confirm('确定要将此页面设置为默认首页吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          self.loading = true;
          PageApi.setPage({
              pageId: pageId
            }, true).then(data => {
              self.loading = false;
              ElMessage ({
                message: '恭喜你，设置成功',
                type: 'success'
              });
              self.getTableList();
            })
            .catch(error => {
              self.loading = false;
            });

        }).catch(() => {

        });
      },

      /*编辑*/
      editClick(pageId) {
        let self = this;
        self.$router.push({
          path: '/page/page/editPage',
          query: {
            pageId: pageId
          }
        })

      }
    }
  };
</script>
<style>

  .diy-preview .diy-phone{
    width: 375px;
    margin-right: 20px;
    flex-shrink: 0;
  }
  .diy-preview .diy-phone .btn-edit-del{
    display: none;
  }
  .diy-preview .diy-phone .diy-phone-container .diy-phone-item > div:hover, .diy-preview .diy-phone .diy-phone-container .diy-phone-item.active > div{
    border: 2px solid #f1f1f2;;
  }
</style>
