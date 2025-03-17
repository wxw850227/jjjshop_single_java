<template>
  <div class="plus-container">
    <!--插件首页-->
    <div class="common-level-rail"></div>

    <div v-for="(item, index) in tableData" :key="index" class="mb16">
      <div class="common-form d-s-c">
        <span>{{ item.name }}</span>
      </div>
      <div class="plus-list">
        <div class="item" v-for="(child, num) in item.children" :key="num">
          <div class="item-box pr">
            <a
              href="javascript:void(0);"
              class="close-btn"
              @click="deleteClick(child)"
            >
              <el-icon :size="20">
                <CloseBold />
              </el-icon>
            </a>
            <a>
              <span class="iconfont icon" :class="hasIcon(child.icon)"></span>
              <div class="ml10">
                <h3>{{ child.name }}</h3>
                <p>{{ child.remark }}</p>
              </div>
            </a>
          </div>
        </div>
        <div class="item">
          <div class="d-s-c mt10">
            <div class="add-item-box d-c-c" @click="addClick(item)">
              <el-icon>
                <Plus />
              </el-icon>
            </div>
            <div class="ml10">
              <p class="f14 gray9">添加插件到此类别下</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--添加-->
    <Add
      v-if="open_add"
      :open_add="open_add"
      :curModel="curModel"
      @closeDialog="closeDialogFunc($event, 'add')"
    ></Add>
  </div>
</template>

<script>
import PlusApi from "@/api/plus.js";
import Add from "./dialog/Add.vue";
import { deepClone } from "@/utils/base.js";
export default {
  components: {
    Add,
  },
  data() {
    return {
      /*是否正在加载*/
      loading: true,
      /*表格数据*/
      tableData: [],
      /*是否打开添加弹窗*/
      open_add: false,
      /*当前编辑的对象*/
      curModel: {},
    };
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*获取列表*/
    getData() {
      let self = this;
      let Params = {};
      PlusApi.pluslist(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data;
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*打开添加*/
    addClick(e) {
      this.curModel = e;
      this.open_add = true;
    },

    closeDialogFunc(e, f) {
      if (f == "add") {
        this.open_add = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
    },
    /*删除插件*/
    deleteClick(row) {
      let self = this;
      ElMessageBox.confirm("删除后不可恢复，确认删除该记录吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          self.loading = true;
          PlusApi.deleteplus(
            {
              id: row.accessId,
            },
            true
          )
            .then((data) => {
              if (data.code == 1) {
                self.loading = false;
                ElMessage({
                  message: data.msg,
                  type: "success",
                });
                self.getData();
              }
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {});
    },
    //判断是否有图标
    hasIcon(e) {
      if (e != null && e.length > 0) {
        return e;
      } else {
        return "icon-chajian1";
      }
    },
  },
};
</script>

<style>
.plus-container {
  min-height: 400px;
}

.plus-container .common-form .close-btn {
  color: #cccccc;
}

.plus-list {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
}

.plus-list .item {
  width: 25%;
}

.plus-list .item .item-box {
  margin: 0 10px 20px;
  border: 1px solid #ffffff;
}

.plus-list .item .item-box .close-btn {
  position: absolute;
  padding-left: 0;
  width: 20px;
  height: 20px;
  top: 20px;
  right: 4px;
  color: #ff0000;
  display: none;
}

.plus-list .item .item-box:hover {
  border-radius: 8px;
  border: 1px dashed #cccccc;
}

.plus-list .item .item-box:hover .close-btn {
  display: block;
}

.plus-list .item .add-item-box {
  width: 40px;
  height: 40px;
  margin-left: 20px;
  border-radius: 8px;
  border: 1px solid #186eeb;
}

.plus-list .item a {
  display: flex;
  height: 60px;
  padding-left: 10px;
  justify-content: flex-start;
  align-items: center;
}

.plus-list .item a h3 {
  font-weight: normal;
  color: #333333;
}

.plus-list .item a:hover h3 {
  color: #3a8ee6;
}

.plus-list .item a p {
  font-size: 12px;
  color: #999999;
}

.plus-list .item .item-box .iconfont {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  line-height: 40px;
  background: #3a8ee6;
}

.plus-list .item a .icon.iconfont {
  color: #ffffff;
  font-size: 22px;
}
</style>
