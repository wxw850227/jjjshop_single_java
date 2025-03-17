<template>
  <div class="spec-many-type">
    <!--规格属性-->
    <div class="spec-wrap">
      <template v-if="form.model.specMany && form.model.specMany.specAttr">
        <div class="mb16 min-spc" :key="attr.groupName" v-for="(attr, index) in form.model.specMany.specAttr">
          <div class="spec-hd">
            <div class="input-box">{{ attr.groupName }}</div>
            <a href="javascript:void(0);" @click="onDeleteGroup(index)"><el-icon><Delete /></el-icon></a>
          </div>
          <div class="spec-bd">
            <div class="item" v-for="(items, i) in attr.specItems" :key="items.specValue">
              <el-tag size="large" closable @close="onDeleteValue(index, i)">{{ items.specValue }}</el-tag>
            </div>
            <div class="item"><el-input  v-model="attr.tempValue" style="width: 160px;"></el-input></div>
            <div class="item"><el-button  @click="onSubmitAddValue(attr)" :loading="attr.loading">添加</el-button></div>
          </div>
        </div>
      </template>
      <!--添加规格-->
      <div v-if="!form.isSpecLocked">
        <el-button size="small" class="el-icon-circle-plus" v-show="showAddGroupBtn" @click="onToggleAddGroupForm">添加规格</el-button>
      </div>
      <!--规格列表-->
      <div class="add-spec mb16" v-show="!showAddGroupBtn">
        <div class="from-box">
          <div class="item">
            <span class="key">规格名：</span>
            <el-input size="small" v-model="addGroupFrom.specName" placeholder="请输入规格名称"></el-input>
          </div>
          <div class="item">
            <span class="key">规格值：</span>
            <el-input size="small" v-model="addGroupFrom.specValue" placeholder="请输入规格值"></el-input>
          </div>
          <el-button type="primary" size="small" :loading="groupLoading" @click="onSubmitAddGroup" plain>确定</el-button>
          <el-button size="small" @click="onToggleAddGroupForm">取消</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PorductApi from '@/api/product.js';
export default {
  data() {
    return {
      /*显示添加规格组按钮*/
      showAddGroupBtn: true,
      /*显示添加规格组表单*/
      showAddGroupForm: false,
      /*新增规格组值*/
      addGroupFrom: {
        specName: '',
        specValue: ''
      },
      groupLoading: false
    };
  },
  inject: ['form'],
  created() {
    /*获取列表*/
    if (this.form.model.specMany && this.form.model.specMany.specList.length > 0) {
      this.buildSkulist();
    }
  },
  methods: {

    /*显示/隐藏添加规则组 */
    onToggleAddGroupForm: function() {
      this.showAddGroupBtn = !this.showAddGroupBtn;
      this.showAddGroupForm = !this.showAddGroupForm;
    },

    /*表单提交：新增规格组 */
    onSubmitAddGroup: function() {
      let self = this;
      if (self.addGroupFrom.specName === '' || self.addGroupFrom.specValue === '') {
        ElMessage ('请填写规则名或规则值');
        return false;
      }
      // 添加到数据库
      self.groupLoading = true;
      let Params = {
        specName: self.addGroupFrom.specName,
        specValue: self.addGroupFrom.specValue
      };
      PorductApi.addSpec(Params, true)
        .then(res => {
          self.groupLoading = false;
          // 记录规格数据
          self.form.model.specMany.specAttr.push({
            groupId: res.data['specId'],
            groupName: self.addGroupFrom.specName,
            specItems: [
              {
                itemId: res.data['specValueId'],
                specValue: self.addGroupFrom.specValue
              }
            ],
            tempValue: '',
            loading: false
          });
          // 清空输入内容
          self.addGroupFrom.specName = '';
          self.addGroupFrom.specValue = '';
          // 隐藏添加规则组
          self.onToggleAddGroupForm();
          // 构建规格组合列表
          self.buildSkulist();
        })
        .catch(error => {
          self.groupLoading = false;
        });
    },

    /*新增规格值*/
    onSubmitAddValue: function(specAttr) {
      let self = this;
      if (!specAttr.hasOwnProperty('tempValue') || specAttr.tempValue === '') {
        ElMessage ('规格值不能为空');
        return false;
      }
      // 添加到数据库
      specAttr.loading = true;
      let Params = {
        specId: specAttr.groupId,
        specValue: specAttr.tempValue
      };
      PorductApi.addSpecValue(Params, true)
        .then(data => {
          specAttr.loading = false;
          // 记录规格数据
          specAttr.specItems.push({
            itemId: data.data['specValueId'],
            specValue: specAttr.tempValue
          });
          // 清空输入内容
          specAttr.tempValue = '';
          // 构建规格组合列表
          self.buildSkulist();
        })
        .catch(error => {
          specAttr.loading = false;
        });
    },

    /*构建规格组合列表*/
    buildSkulist: function() {

      let self = this;
      let specAttr = self.form.model.specMany.specAttr;
      let specArr = [];
      for (let i = 0; i < specAttr.length; i++) {
        specArr.push(specAttr[i].specItems);
      }

      let specListTemp = self.calcDescartes(specArr);

      let specList = [];
      for (let i = 0; i < specListTemp.length; i++) {
        let rows = [];
        let specSkuIdAttr = [];
        if (!Array.isArray(specListTemp[i])) {
          rows.push(specListTemp[i]);
        } else {
          rows = specListTemp[i];
        }
        for (let j = 0; j < rows.length; j++) {
          specSkuIdAttr.push(rows[j].itemId);
        }
        specList.push({
          productSkuId: 0,
          specSkuId: specSkuIdAttr.join('_'),
          rows: rows,
          specForm: {}
        });
      }

      // 合并旧sku数据
      if (self.form.model.specMany.specList.length > 0 && specList.length > 0) {
        for (let i = 0; i < specList.length; i++) {
          let overlap = self.form.model.specMany.specList.filter(function(val) {
            return val.specSkuId === specList[i].specSkuId;
          });
          if (overlap.length > 0) {
            specList[i].specForm = overlap[0].specForm;
            specList[i].productSkuId=overlap[0].productSkuId;
          }
        }
      }

      self.form.model.specMany.specList = specList;
    },

    /*规格组合*/
    calcDescartes: function(array) {
      if (array.length < 2) return array[0] || [];
      return [].reduce.call(array, function(col, set) {
        let res = [];
        col.forEach(function(c) {
          set.forEach(function(s) {
            let t = [].concat(Array.isArray(c) ? c : [c]);
            t.push(s);
            res.push(t);
          });
        });
        return res;
      });
    },

    /*删除规格组事件*/
    onDeleteGroup: function(index) {
      var self = this;
      ElMessageBox.confirm('删除后不可恢复，确认删除该记录吗?', '提示', {
          type: 'warning'
        })
        .then(() => {
          // 删除指定规则组
          self.form.model.specMany.specAttr.splice(index, 1);
          // 构建规格组合列表
          self.buildSkulist();
        });
    },

    /*删除规格值值事件*/
    onDeleteValue: function(index, itemIndex) {
      let self = this;

      if(self.form.isSpecLocked){
        ElMessage ({
          message: '本商品正在搞活动，不能删除规格！',
          type: 'warning'
        });
        return;
      }

      ElMessageBox.confirm('删除后不可恢复，确认删除该记录吗?', '提示', {
          type: 'warning'
        })
        .then(() => {
          // 删除指定规则组
          self.form.model.specMany.specAttr[index].specItems.splice(itemIndex, 1);
          // 构建规格组合列表
          self.buildSkulist();
        });
    }
  }
};
</script>

<style scoped="scoped">
.spec-many-type {
  margin-left: 180px;
  margin-top: 16px;
  padding: 20px;
  border: 1px solid #e5ecf4;
  background: #f6f9fc;
}
.spec-wrap .spec-hd {
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  font-weight: bold;
}
.spec-wrap .spec-hd .el-icon-delete-solid {
  font-size: 16px;
  color: #999999;
}
.spec-wrap .min-spc {
  border: 1px solid #dfecf8;
}
.spec-wrap .spec-bd {
  padding: 5px;
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
  border-top: 1px solid #dfecf8;
  background: #ffffff;
}
.spec-wrap .spec-bd .el-tag {
  color: #333333;
}
.spec-wrap .spec-bd .item {
  position: relative;
  padding: 5px;
}
.spec-wrap .spec-bd .item input {
  padding-right: 30px;
}
.spec-wrap .spec-hd a,
.spec-wrap .spec-hd .svg-icon,
.spec-wrap .spec-bd .item .svg-icon {
  display: block;
  width: 16px;
  height: 16px;
}
.spec-wrap .spec-bd .item a {
  position: absolute;
  top: 6px;
  right: 5px;
  width: 30px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.add-spec .from-box {
  display: flex;
  justify-content: flex-start;
}
.add-spec .item {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 200px;
  margin-right: 20px;
}
.add-spec .item .key {
  display: block;
  white-space: nowrap;
}
</style>
