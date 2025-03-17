<template>
  <el-dialog title="选择分类" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
    :close-on-press-escape="false">

    <!--内容-->
    <div class="product-content">
      <el-cascader-panel ref="nodes" :options="options" v-model="listData"
        :props="{multiple:true,value:'categoryId',label:'name',children:'children'}" :show-all-levels="false"
        :selectable="selectableFunc">
      </el-cascader-panel>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="small" @click="dialogVisible=false">取 消</el-button>
        <el-button size="small" type="primary" @click="addClerk">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
  import ProductApi from '@/api/product.js';
  export default {
    data () {
      return {
        /*是否加载完成*/
        loading: true,
        formInline: {},
        multipleSelection: [],
        /*左边长度*/
        formLabelWidth: '120px',
        /*是否显示*/
        dialogVisible: false,
        /*结果类别*/
        type: 'error',
        /*传出去的参数*/
        params: null,
        options: [],
        listData: []
      };
    },
    props: ['isCategory', 'categoryList'],
    watch: {
      isCategory: function (n, o) {
        if (n != o) {
          if (n) {
            this.dialogVisible = n;
            this.type = 'error';
            this.params = null;
            this.getData();
          } else {
            this.dialogVisible = n;
          }
        }
      },
      categoryList: function (n, o) {
        if (n != o) {
          if (n) {
            this.listData = [];
            if (n.first.length > 0) {
              n.first.forEach((item, index) => {
                this.listData.push([item.categoryId])
              })
            }
            if (n.second.length > 0) {
              n.second.forEach((item, index) => {
                this.listData.push([item.parentId, item.categoryId])
              })
            }
          }
        }
      }
    },
    created () {

    },
    methods: {

      /*是否可以勾选*/
      selectableFunc (e) {
        if (typeof e.noChoose !== 'boolean') {
          return true;
        }
        return e.noChoose;
      },

      /*选择第几页*/
      handleCurrentChange (val) {
        this.curPage = val;
        this.getData();
      },

      /*每页多少条*/
      handleSizeChange (val) {
        this.curPage = 1;
        this.pageSize = val;
        this.getData();
      },

      /*获取商品列表*/
      getData () {
        let self = this;
         ProductApi.catList({},true)
           .then(res => {
             self.options = res.data;
           })
           .catch(error => {

           });
      },

      /*单选*/
      SingleFunc (row) {
        this.multipleSelection = [row];
        this.addClerk();
      },

      //点击确定
      addClerk () {
        let self = this;
        let params = {
          first: [],
          second: []
        }
        console.log(self.$refs.nodes.getCheckedNodes())
        let checkedNodes = self.$refs.nodes.getCheckedNodes(false, false);
        checkedNodes.forEach((item, index) => {
          if (item.parent == null) {
            params.first.push({
              categoryId: item.value,
              name: item.label
            })
          } else {
            params.second.push({
              categoryId: item.value,
              name: item.label,
              parent: item.parent.label,
              parentId: item.parent.value
            })
          }
        })
        self.$emit('close', params)
      },

      /*关闭弹窗*/
      dialogFormVisible () {
        this.$emit('closeDialog', {
          type: this.type,
          openDialog: false,
          params: this.params
        });
      },

      /*监听复选按钮选中事件*/
      tableCurrentChange (val) {
        this.multipleSelection = val;
      }
    }
  };
</script>

<style>
  .no-list .el-checkbox {
    display: none;
  }
</style>
