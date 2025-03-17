<template>
  <div class="product-list">
    <!--搜索表单-->
    <div class="common-seach-wrap">
      <el-tabs v-model="activeName" @tab-change="handleClick">
        <el-tab-pane label="出售中" name="sell">
          <template #label>
            <span>出售中
              <el-tag size="small">{{ productCount.sell }}</el-tag>
            </span>
          </template>
        </el-tab-pane>
        <el-tab-pane label="仓库中" name="lower">
          <template #label>
            <span>仓库中 <el-tag size="small">{{ productCount.lower }}</el-tag></span>
          </template>
        </el-tab-pane>
        <el-tab-pane label="回收站" name="recovery">
          <template #label>
            <span>
              回收站
              <el-tag size="small">{{ productCount.recovery }}</el-tag>
            </span>
          </template>
        </el-tab-pane>
        <el-tab-pane label="库存紧张" name="stock">
          <template #label>
            <span>
              库存紧张
              <el-tag size="small">{{ productCount.stock }}</el-tag>
            </span>
          </template>
        </el-tab-pane>
        <el-tab-pane label="已售罄" name="over">
          <template #label>
          <span>
            已售罄
            <el-tag size="small">{{ productCount.over }}</el-tag>
          </span>
          </template>
        </el-tab-pane>
      </el-tabs>
      <el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="商品分类">
          <el-select size="small" v-model="searchForm.categoryId" placeholder="所有分类">
            <el-option label="全部" value="0"></el-option>
            <el-option v-for="(item, index) in categoryList" :key="index" :label="item.name"
              :value="item.categoryId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品名称"><el-input size="small" v-model="searchForm.productName"
            placeholder="请输入商品名称"></el-input></el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" icon="Search" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!--添加产品-->
    <div class="common-level-rail"><el-button size="small" type="primary" icon="Plus" @click="addClick"
        v-auth="'/product/product/add'">添加产品</el-button></div>

    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
          <el-table-column prop="productName" label="产品" width="400px">
            <template #default="scope">
              <div class="product-info">
                <div class="pic"><img v-img-url="scope.row.imagePath" alt="" /></div>
                <div class="info">
                  <div class="name">{{ scope.row.productName }}</div>
                  <div class="price">￥{{ scope.row.productPrice }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="categoryName" label="分类"></el-table-column>
          <el-table-column prop="salesActual" label="实际销量"></el-table-column>
          <el-table-column prop="productStock" label="库存"></el-table-column>
          <el-table-column prop="viewTimes" label="点击数"></el-table-column>
          <el-table-column label="商品属性">
            <template #default="scope">
              <span v-if="scope.row.isVirtual == 0">实物商品</span>
              <span v-if="scope.row.isVirtual == 1" class="green">虚拟商品</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="发布时间"></el-table-column>
          <el-table-column prop="productSort" label="排序"></el-table-column>
          <el-table-column fixed="right" label="操作" width="210">
            <template #default="scope">
              <el-button @click="editClick(scope.row)" type="text" size="small"
                v-auth="'/product/product/edit'">编辑</el-button>
              <el-button v-if="activeName == 'sell'" @click="changeStatus(scope.row, 20)" type="text" size="small"
                v-auth="'/product/product/edit'">下架</el-button>
              <el-button v-if="activeName == 'lower'" @click="changeStatus(scope.row, 10)" type="text" size="small"
                v-auth="'/product/product/edit'">上架</el-button>
              <el-button v-if="activeName == 'recovery'" @click="changeStatus(scope.row, 20)" type="text" size="small"
                v-auth="'/product/product/edit'">恢复</el-button>
              <el-button @click="delClick(scope.row)" type="text" size="small"
                v-auth="'/product/product/delete'">删除</el-button>
              <el-button @click="copyClick(scope.row)" type="text" size="small"
                v-auth="'/product/product/add'">复制</el-button>
              <el-button @click="linkClick(scope.row.productId)" type="text" size="small">链接</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <!--分页-->
    <div class="pagination">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background
        :current-page="curPage" :page-size="pageSize" layout="total, prev, pager, next, jumper"
        :total="totalDataNumber"></el-pagination>
    </div>
  </div>
</template>

<script>
import PorductApi from '@/api/product.js';
export default {
  components: {},
  data() {
    return {
      /*切换菜单*/
      activeName: 'sell',
      /*切换选中值*/
      activeIndex: '0',
      /*是否正在加载*/
      loading: true,
      /*一页多少条*/
      pageSize: 15,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      /*搜索参数*/
      searchForm: {
        productName: '',
        categoryId: ''
      },
      /*列表数据*/
      tableData: [],
      /*全部分类*/
      categoryList: [],
      productCount: {},
    };
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.loading = true;
      self.curPage = val;
      self.getData();
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.pageSize = val;
      this.getData();
    },

    /*切换菜单*/
    handleClick(tab) {
      let self = this;
      self.curPage = 1;
      self.activeName = tab;
      self.getData();
    },

    /*获取列表*/
    getData() {
      let self = this;
      let Params = self.searchForm;
      Params.pageIndex = self.curPage;
      Params.pageSize = self.pageSize;
      Params.type = self.activeName;
      self.loading = true;
      PorductApi.productList(Params, true)
        .then(res => {
          self.loading = false;
          self.tableData = res.data.productList.records;
          self.categoryList = res.data.categoryList;
          self.totalDataNumber = res.data.productList.total;
          self.productCount = res.data.productCount;
        })
        .catch(error => {
          self.loading = false;
        });
    },

    /*搜索查询*/
    onSubmit() {
      this.curPage = 1;
      this.getData();
    },

    /*打开添加*/
    addClick() {
      this.$router.push('/product/product/add');
    },

    /*打开编辑*/
    editClick(row) {
      console.log(row);
      this.$router.push({
        path: '/product/product/edit',
        query: {
          productId: row.productId,
          scene: 'edit'
        }
      });
    },

    linkClick: function (val) {
      let self = this;
      let copyText = 'pages/product/detail/detail?productId=' + val;
      self.$copyText(copyText).then(function (e) {
        ElMessage({
          showClose: true,
          message: '复制成功',
          type: 'success'
        });
      }, function (e) {
        ElMessage({
          showClose: true,
          message: '复制失败',
          type: 'error'
        });
      })
    },
    /*打开复制*/
    copyClick(row) {
      this.$router.push({
        path: '/product/product/edit',
        query: {
          productId: row.productId,
          scene: 'copy'
        }
      });
    },
    /*删除*/
    delClick: function (row) {
      let self = this;
      let msg = '';
      if (row.productStatus == 30) {
        msg = '删除后不可恢复，确认删除该记录吗?';
      } else {
        msg = '删除后将移动到回收站，确定删除该记录吗?';
      }
      ElMessageBox.confirm(msg, '提示', {
          type: 'warning'
        })
        .then(() => {
          PorductApi.delProduct({
            productId: row.productId
          }).then(data => {
            ElMessage({
              message: '删除成功',
              type: 'success'
            });
            self.getData();
          });
        });
    },
    /*修改状态*/
    changeStatus(item, value) {
      let self = this;
      let msg = '';
      if (value == 10) {
        msg = '确认要上架该商品吗?';
      } else {
        msg = '确认要下架该商品吗?';
        if (self.activeName == 'recovery') {
          msg = '确认要移动该商品到仓库吗?';
        }
      }
      ElMessageBox.confirm(msg, '提示', {
        type: 'warning'
      })
        .then(() => {
          PorductApi.changeStatus({
            productId: item.productId,
            state: value
          }).then(data => {
            ElMessage({
              message: '操作成功',
              type: 'success'
            });
            self.getData();
          });
        });
    },
  }
};
</script>

<style></style>
