<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" :rules="formRules" label-width="200px">
      <!--添加门店-->
      <div class="common-form">新增运费模版</div>
      <el-form-item label="模版名称" prop="name">
        <el-input v-model="form.name" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="计费方式">
        <div>
          <el-radio v-model="form.method" :label="10">按件数</el-radio>
          <el-radio v-model="form.method" :label="20">按重量</el-radio>
        </div>
      </el-form-item>

      <el-form-item label="配送区域及运费">
        <el-table :data="form.rule" border style="width: 100%;">
          <el-table-column label="可配送区域">
            <template #default="scope">
              <div class="area-list">
                <span v-if="scope.row.citys.length == cityCount">全国</span>
                <span v-else v-for="(province, index) in scope.row.showData" :key="index" class="pr16">
                  {{ province.name }}
                  <template v-if="!province.isAllCitys">
                    (
                    <span class="am-link-muted gray9">
                      <em v-for="(city, city_index) in province.citys" :key="city_index">
                        <span>{{ city.name }}</span>
                        <span v-if="city_index + 1 < province.citys.length">、</span>
                      </em>
                    </span>
                    )
                  </template>
                </span>
              </div>
              <a href="javascript:void(0);" @click="el = editAreaClick(scope.$index)">编辑</a>
            </template>
          </el-table-column>
          <el-table-column prop="first" :label="tableHeadName.first" width="100px">
            <template #default="scope">
              <el-input v-model="scope.row['first']"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="firstFee" :label="tableHeadName.firstFee" width="100px">
            <template #default="scope">
              <el-input v-model="scope.row['firstFee']"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="additional" :label="tableHeadName.additional" width="100px">
            <template #default="scope">
              <el-input v-model="scope.row['additional']"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="additionalFee" :label="tableHeadName.additionalFee" width="100px">
            <template #default="scope">
              <el-input v-model="scope.row['additionalFee']"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="60px">
            <template #default="scope">
              <a href="javascript:void(0);" @click="deleteAreaClick(scope.$index)">删除</a>
            </template>
          </el-table-column>
        </el-table>
        <div>
          <el-button @click="addAreaClick" type="text" size="small">+点击添加可配送区域和运费</el-button>
        </div>
      </el-form-item>

      <el-form-item label="排序">
        <el-input v-model="form.sort" type="number" class="max-w460"></el-input>
        <div class="tips">数字越小越靠前</div>
      </el-form-item>

      <!--提交-->
      <div class="common-button-wrapper">
        <el-button type="primary" @click="onSubmit" :loading="loading">提交</el-button>
      </div>
    </el-form>

    <!--区域 -->
    <Areas v-if="show_area" :show_area="show_area" :areaModel="areaModel" :areas="options" @closeArea="closeAreaFunc">
    </Areas>
  </div>
</template>

<script>
  import SettingApi from '@/api/setting.js';
  import Areas from './dialog/Area.vue';
  import {
    deepClone
  } from '@/utils/base';
  export default {
    components: {
      Areas: Areas
    },
    data() {
      return {
        /*form表单数据*/
        form: {
          method: 10,
          name: '',
          sort: 1,
          rule: []
        },
        formRules: {
          name: [{
            required: true,
            message: ' ',
            trigger: 'blur'
          }]
        },
        loading: false,
        /*区域数据源*/
        options: [],
        /*区域数据源*/
        optionsMap: {},
        /*区域选中的model*/
        areaModel: {
          index: 0
        },
        /*显示区域选中*/
        show_area: false,
        /*表格标题名称*/
        tableHeadName: {
          first: '首件(个)',
          firstFee: '运费(元)',
          additional: '续件(个)',
          additionalFee: '续费(元)'
        },
        cityCount: 0,
        formData: []
      };
    },
    watch: {
      'form.method': function(n, o) {
        if (n == 10) {
          this.tableHeadName = {
            first: '首件(个)',
            firstFee: '运费(元)',
            additional: '续件(个)',
            additionalFee: '续费(元)'
          };
        } else {
          this.tableHeadName = {
            first: '首重(Kg)',
            firstFee: '运费(元)',
            additional: '续重(Kg)',
            additionalFee: '续费(元)'
          };
        }
      },
      'form.rule': {
        handler(val, oldVal) {},
        // 这里是关键，代表递归监听 items 的变化
        deep: true
      }
    },
    created() {
      // 取到路由带过来的参数
      this.getData();
    },

    methods: {
      getData() {
        let self = this;
        SettingApi.toAddDelivery({}, true)
          .then(res => {
            self.options = res.data.regionData;
            self.cityCount = res.data.cityCount;

            //区域转数组
            self.options.forEach(function(area) {
              self.optionsMap[area.id] = area;
            });

            if (!self.formData.length) return false;
            self.formData.forEach(function(form) {
              // 转换为整数型
              for (var key in form.citys) {
                if (form.citys.hasOwnProperty(key)) {
                  form.citys[key] = parseInt(form.citys[key]);
                }
              }
              form['showData'] = self.getShowData({
                province: form.province,
                citys: form.citys
              });
              self.form.rule.push(form);
            });
          })
          .catch(error => {
            console.log(error);
          });
      },

      // 格式化选中的区域id
      getShowData: function(checkedData) {
        var self = this;
        var formData = {};
        checkedData.province.forEach(function(provinceId) {
          var province = self.optionsMap[provinceId],
            citys = [],
            cityCount = 0;
          for (var cityIndex in province.children) {
            if (province.children.hasOwnProperty(cityIndex)) {
              var cityItem = province.children[cityIndex];
              if (self.inArray(cityItem.id, checkedData.citys)) {
                citys.push({
                  id: cityItem.id,
                  name: cityItem.name
                });
              }
              cityCount++;
            }
          }
          formData[province.id] = {
            id: province.id,
            name: province.name,
            citys: citys,
            isAllCitys: citys.length === cityCount
          };
        });
        return formData;
      },

      // 数组中是否存在指定的值
      inArray: function(val, array) {
        return array.indexOf(val) > -1;
      },

      /*添加区域*/
      addAreaClick(e) {
        this.areaModel.index = this.form.rule.length;
        this.areaModel.type = 'add';
        this.show_area = true;
      },

      /*编辑区域*/
      editAreaClick(e) {
        this.areaModel.index = e;
        this.areaModel.type = 'edit';
        this.show_area = true;
      },

      /*关闭区域选中弹窗*/
      closeAreaFunc(e) {
        let self = this;
        this.show_area = e.statu;
        if (e.type != 'confirm') {
          return;
        }
        this.options = e.this_area;
        let provinces = [];
        let citys = [];
        e.this_area.forEach(function(province, index) {
          if ((province.checked == true || province.indeterminate == true) && province.index.indexOf(self.areaModel
              .index) != -1) {
            provinces.push(province['id']);
            province.children.forEach(function(city) {
              if (city.checked == true && city.index == self.areaModel.index) {
                citys.push(city['id']);
              }
            });
          }
        });
        if (this.areaModel.type == 'add') {
          this.form.rule.push({
            first: '',
            firstFee: '',
            additional: '',
            additionalFee: '',
            citys: citys,
            showData: this.getShowData({
              province: provinces,
              citys: citys
            })
          });
        }
        if (this.areaModel.type == 'edit') {
          this.form.rule[this.areaModel.index]['showData'] = this.getShowData({
            province: provinces,
            citys: citys
          });
          console.log(this.form);
          //设置rule的region
          this.form.rule[this.areaModel.index]['citys'] = citys;
        }
        //更新表格变通做法
        this.form.rule.push([]);
        this.form.rule.pop();
      },

      /*删除*/
      deleteAreaClick(e) {
        let self = this;

        self.options.forEach(function(province) {
          if (self.inArray(province['id'], self.form.rule[e].province)) {
            province.checked = false;
            province.index = null;
          }
          province.children.forEach(function(city) {
            if (self.inArray(city['id'], self.form.rule[e].citys)) {
              city.checked = false;
              city.index = null;
            }
          });
        });

        self.form.rule.splice(e, 1);
      },

      //修改提交表单
      onSubmit() {
        let self = this;
        let form = deepClone(self.form);

        //移除showData
        form.rule.forEach(function(rule) {
          delete rule.showData;
        });

        self.$refs.form.validate(valid => {
          if (valid) {
            if (form.rule.length == 0) {
              ElMessage ({
                message: '请添加配送区域和运费',
                type: 'error'
              });
              return false;
            }

            for (let key in form.rule) {
              if (!form.rule[key].hasOwnProperty('first') || !(form.rule[key].first > 0)) {
                ElMessage ({
                  message: '首件/首重不能为空',
                  type: 'error'
                });
                return false;
              }
              if (!form.rule[key].hasOwnProperty('firstFee') || form.rule[key].firstFee == '') {
                ElMessage ({
                  message: '运费不能为空',
                  type: 'error'
                });
                return false;
              }
              if (!form.rule[key].hasOwnProperty('additional') || !(form.rule[key].additional > 0)) {
                ElMessage ({
                  message: '续件/续重不能为空',
                  type: 'error'
                });
                return false;
              }
              if (!form.rule[key].hasOwnProperty('additionalFee') || form.rule[key].additionalFee == '') {
                ElMessage ({
                  message: '续费不能为空',
                  type: 'error'
                });
                return false;
              }
              form.rule[key]['region'] = form.rule[key]['citys'].join(',');
              delete form.rule[key]['citys'];
            }
            self.loading = true;
            SettingApi.addDelivery(form, true)
              .then(data => {
                self.loading = false;
                ElMessage ({
                  message: '恭喜你，修改成功',
                  type: 'success'
                });
                self.$router.push('/setting/delivery/index');
              })
              .catch(error => {
                self.loading = false;
              });
          }
        });
      }
    }
  };
</script>

<style>
  .tips {
    color: #ccc;
  }

  .el-table__header-wrapper {
    line-height: 23px;
  }

  .el-table .area-list {
    font-size: 12px;
  }

  .el-table .area-list .province {
    padding-right: 10px;
    font-weight: bold;
  }

  .el-table .area-list .city {
    display: inline-block;
    white-space: nowrap;
    padding-right: 4px;
    color: #999;
  }
</style>
