<template>
  <div class="pb50" v-loading="loading">
    <!--内容-->
    <div class="product-content">
      <!--基本信息-->
      <div class="common-form">基本信息</div>
      <div class="table-wrap">
        <el-row>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">订单号：</span>
              {{ detail.orderNo }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">买家：</span>
              {{ detail.nickName }}
              <span>用户ID：({{ detail.userId }})</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">订单金额 (元)：</span>
              {{ detail.orderPrice }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">运费金额 (元)：</span>
              {{ detail.expressPrice }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16" v-if="detail.couponMoney > 0">
              <span class="gray9">优惠券抵扣 (元)：</span>
              {{ detail.couponMoney }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16" v-if="detail.pointsMoney > 0">
              <span class="gray9">积分抵扣 (元)：</span>
              {{ detail.pointsMoney }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16" v-if="detail.fullreduceMoney > 0">
              <span class="gray9">满减金额 (元)：</span>
              {{ detail.fullreduceMoney }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16" v-if="detail.productReduceMoney > 0">
              <span class="gray9">商品满减 (元)：</span>
              {{ detail.productReduceMoney }}
            </div>
          </el-col>

          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">实付款金额 (元)：</span>
              {{ detail.payPrice }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">支付方式：</span>
              {{ detail.payTypeText }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">配送方式：</span>
              {{ detail.deliveryTypeText }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">交易状态：</span>
              {{ detail.orderStatusText }}
            </div>
          </el-col>
          <!-- <el-col :span="5" v-if="detail.orderSource==80">
            <div class="pb16">
              <span class="gray9">定金：</span>
              {{ detail.advance.payPrice }}
            </div>
          </el-col>
          <el-col :span="5" v-if="detail.orderSource==80">
            <div class="pb16">
              <span class="gray9">尾款：</span>
              {{ detail.payPrice }}
            </div>
          </el-col> -->
          <!-- <el-col :span="5" v-if="detail.orderSource==80&&detail.advance.reduceMoney">
            <div class="pb16">
              <span class="gray9">尾款立减：</span>
              {{ detail.advance.reduceMoney }}
            </div>
          </el-col> -->
          <el-col :span="5" v-if="detail.payStatus == 10 && detail.orderStatus == 10 && detail.orderSource == 10" v-auth="'/order/order/updatePrice'">
            <el-button @click="editClick(detail)" size="small">修改价格</el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 编辑 -->
      <Add v-if="open_edit" :open_edit="open_edit" :order="userModel" @close="closeDialogFunc($event, 'edit')"></Add>
      <!--商品信息-->
      <div class="common-form mt16">商品信息</div>
      <div class="table-wrap">
        <el-table size="small" :data="detail.productList" border style="width: 100%">
          <el-table-column prop="productName" label="商品" width="400">
            <template #default="scope">
              <div class="product-info">
                <div class="pic"><img v-img-url="scope.row.imagePath" /></div>
                <div class="info">
                  <div class="name">{{ scope.row.productName }}</div>
                  <!-- <div class="gray9" v-if="scope.row.productAttr!=''">{{scope.row.productAttr}}</div>
                  <div class="orange" v-if="scope.row.refund">
                    {{ scope.row.refund.type.text }}-{{ scope.row.refund.status.text }}
                  </div> -->
                  <div class="price">
                    <span :class="{ 'text-d-line': scope.row.isUserGrade == 1, gray6: scope.row.isUserGrade != 1 }">￥ {{ scope.row.productPrice }}</span>
                    <span class="ml10" v-if="scope.row.isUserGrade == 1">会员折扣价：￥ {{ scope.row.gradeProductPrice }}</span>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="productNo" label="商品编码"></el-table-column>
          <el-table-column prop="productWeight" label="重量 (Kg)"></el-table-column>
          <el-table-column prop="totalNum" label="购买数量">
            <template #default="scope">
              <p>x {{ scope.row.totalNum }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="totalPrice" label="商品总价(元)">
            <template #default="scope">
              <p>￥ {{ scope.row.totalPrice }}</p>
            </template>
          </el-table-column>
          <!--表单信息-->
          <!--         <el-table-column prop="totalPrice" label="表单信息">
            <template #default="scope">
              <div v-if="scope.row.table_record_id > 0">
                <div v-for="(item, index) in scope.row.tableData" :key="index">
                  {{item.name}}:{{item.value}}
                </div>
              </div>
            </template>
          </el-table-column> -->
        </el-table>
      </div>
      <!-- 收货信息 -->
      <div v-if="detail.deliveryType == 10">
        <div class="common-form mt16">收货信息</div>
        <div class="table-wrap">
          <el-row>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">收货人：</span>
                {{ detail.address.name }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">收货电话：</span>
                {{ detail.address.phone }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">收货地址：</span>
                {{ detail.address.region.province }} {{ detail.address.region.city }} {{ detail.address.region.region }} {{ detail.address.detail }}
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="25">
              <div class="pb16">
                <span class="gray9">备注：</span>
                {{ detail.buyerRemark }}
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 自提门店信息 -->
      <!--无需发货-->
      <template v-if="detail.deliveryType == 30">
        <div class="common-form mt16">用户信息</div>
        <div class="table-wrap">
          <el-row>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">联系手机：</span>
                {{ detail.mobile }}
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="25">
              <div class="pb16">
                <span class="gray9">备注：</span>
                {{ detail.buyerRemark }}
              </div>
            </el-col>
          </el-row>
        </div>
      </template>
      <!--付款信息-->
      <div class="table-wrap" v-if="detail.payStatus == 20">
        <div class="common-form mt16">付款信息</div>
        <div class="table-wrap">
          <el-row>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">应付款金额：</span>
                {{ detail.payPrice }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">支付方式：</span>
                {{ detail.payTypeText }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">支付流水号：</span>
                {{ detail.transactionId }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">付款状态：</span>
                {{ detail.payStatusText }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">付款时间：</span>
                {{ detail.payTime }}
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <!--  用户取消订单 -->
      <div v-if="detail.payStatus == 20 && detail.orderStatus == 21" v-auth="'/order/operate/confirmCancel'">
        <div class="common-form mt16">用户取消订单</div>
        <p class="red pb16">当前买家已付款并申请取消订单，请审核是否同意，如同意则自动退回付款金额（微信支付原路退款）并关闭订单。</p>
        <el-form size="small" ref="forms" :model="forms">
          <el-form-item label="审核状态">
            <div>
              <el-radio v-model="forms.isCancel" :label="1">同意</el-radio>
              <el-radio v-model="forms.isCancel" :label="0">拒绝</el-radio>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <!--发货信息-->
      <div v-if="detail.payStatus == 20 && detail.deliveryStatus == 10 && detail.deliveryType == 10 && [20, 21].indexOf(detail.orderStatus) === -1 && (!detail.assembleStatus || detail.assembleStatus == 20)" v-auth="'/order/order/delivery'">
        <div v-if="detail.deliveryStatus == 10">
          <!-- 去发货 -->
          <div class="common-form mt16">去发货</div>
          <el-form size="small" ref="form" :model="form" label-width="100px">
            <el-form-item label="物流公司">
              <el-select v-model="form.expressId" placeholder="请选择快递公司">
                <el-option :label="item.expressName" v-for="(item, index) in expressList" :key="index" :value="item.expressId"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="物流单号"><el-input v-model="form.expressNo" class="max-w460"></el-input></el-form-item>
          </el-form>
        </div>
        <div v-else>
          <div class="common-form mt16">发货信息</div>
          <div class="table-wrap">
            <el-row>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">物流公司：</span>
                  {{ detail.express.expressName }}
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">物流单号：</span>
                  {{ detail.expressNo }}
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">发货状态：</span>
                  {{ detail.deliveryStatusText }}
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">发货时间：</span>
                  {{ detail.deliveryTime }}
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
      <!--取消信息-->
      <div class="table-wrap" v-if="detail.orderStatus == 20 && detail.cancelRemark != ''">
        <div class="common-form mt16">取消信息</div>
        <div class="table-wrap">
          <el-row>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">商家备注：</span>
                {{ detail.cancelRemark }}
              </div>
            </el-col>
          </el-row>
        </div>
      </div>

      <!--门店自提核销-->
      <div v-if="detail.deliveryType == 20 && detail.payStatus == 20 && detail.orderStatus != 21 && detail.orderStatus != 20" v-auth="'/order/operate/extract'">
        <div class="common-form mt16">门店自提核销</div>
        <div v-if="detail.deliveryStatus == 10">
          <el-form size="small" ref="extractForm" :model="extractForm" label-width="100px">
            <el-form-item label="门店核销员">
              <el-select v-model="extractClerkId" placeholder="点击选择">
                <el-option :label="item.realName" v-for="(item, index) in shopClerkList" :key="item.clerkId" :value="item.clerkId"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="买家取货状态 "><el-radio v-model="extractForm.order.extractStatus" :label="1">已取货</el-radio></el-form-item>
            <el-form-item><el-button type="primary" @click="onExtract(detail.orderId)">确认核销</el-button></el-form-item>
          </el-form>
        </div>
        <div v-else class="table-wrap">
          <template v-if="detail.extractClerkId">
            <el-row>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">自提门店名称：</span>
                  {{ detail.extractStoreName }}
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">核销员：</span>
                  {{ detail.extractClerkName }}
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">核销状态：</span>
                  <template v-if="detail.deliveryStatus == 20">
                    已核销
                  </template>
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">核销时间：</span>
                  {{ detail.deliveryTime }}
                </div>
              </el-col>
            </el-row>
          </template>
        </div>
      </div>
      <div
        v-if="detail.deliveryType == 30 && detail.payStatus == 20 && detail.orderStatus != 21 && detail.orderStatus != 20"
        v-auth="'/order/order/delivery'">
        <div class="common-form mt16">虚拟商品发货</div>
        <div v-if="detail.deliveryStatus == 10">
          <el-form size="small" ref="virtualForm" :model="virtualForm" label-width="100px">
            <el-form-item label="发货信息">
              <el-input v-model="virtualForm.virtualContent" class="max-w460"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onVirtual(detail.orderId)">确认发货</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-else class="table-wrap">
          <template v-if="detail.virtualContent">
            <el-row>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">发货信息：</span>
                  {{ detail.virtualContent }}
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">发货状态：</span>
                  <template v-if="detail.deliveryStatus == 20">
                    已发货
                  </template>
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">发货时间：</span>
                  {{ detail.deliveryTime }}
                </div>
              </el-col>
            </el-row>
          </template>
        </div>
      </div>
   
    <div class="common-button-wrapper">
      <el-button size="small" type="info" @click="cancelFunc">返回上一页</el-button>
      <!--确认发货-->
      <template v-if="detail.payStatus == 20 && detail.deliveryType == 10 && [20, 21].indexOf(detail.orderStatus) === -1 && (!detail.assembleStatus || detail.assembleStatus == 20)"  v-auth="'/order/order/delivery'">
        <el-button size="small" type="primary" @click="onSubmit" v-if="detail.deliveryStatus == 10">确认发货</el-button>
      </template>
      <!--用户取消-->
     <template v-if="detail.payStatus == 20 && detail.orderStatus == 21">
        <el-button size="small" type="primary" @click="confirmCancel()">确认审核</el-button>
      </template>
    </div>
    </div>
    <changeAddress v-if="addressData != null" :isChange="isChange" :addressData="addressData" @closeDialog="closeAddress"></changeAddress>
  </div>
</template>

<script>
import OrderApi from '@/api/order.js';
import changeAddress from '@/components/order/changeAddress.vue';
import Add from './dialog/Add.vue';
import { deepClone } from '@/utils/base.js';
export default {
  components: {
    Add,
    changeAddress
  },
  data() {
    return {
      active: 0,
      /*是否加载完成*/
      loading: true,
      /*订单数据*/
      detail: {
        payStatus: [],
        payType: [],
        deliveryType: [],
        user: {},
        address: [],
        product: [],
        orderStatus: [],
        extract: [],
        extractStoreId: [],
        express: [],
        deliveryStatus: [],
        extractClerk: {}
      },
      extractClerkId: '',
      /*是否打开添加弹窗*/
      open_add: false,
      /*一页多少条*/
      pageSize: 20,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      /*发货*/
      form: {
        /*订单ID*/
        expressId: null,
        /*订单号*/
        expressNo: ''
      },
      forms: {
        is_cancel: 1
      },
      extractForm: {
        order: {
          extractStatus: 1
        }
      },
      /*虚拟商品发货*/
      virtualForm: {
        virtualContent: ''
      },
      order: {},
      deliveryType: 0,
      /*配送方式*/
      exStyle: [],
      /*门店列表*/
      shopList: [],
      /*当前编辑的对象*/
      userModel: {},
      /*时间*/
      createTime: '',
      /*快递公司列表*/
      expressList: [],
      shopClerkList: [],
      isChange: false,
      /*是否打开编辑弹窗*/
      open_edit: false,
      addressData:null
    };
  },
  created() {
    /*获取列表*/
    this.getParams();
  },
  methods: {
    next() {
      if (this.active++ > 4) this.active = 0;
    },
    /*获取参数*/
    getParams() {
      let self = this;
      // 取到路由带过来的参数
      const params = this.$route.query.orderId;
      OrderApi.orderdetail(
        {
          orderId: params
        },
        true
      )
        .then(res => {
          self.loading = false;
          self.detail = res.data;
          console.log(self.detail.deliveryStatus == 10);
          self.expressList = res.data.expressList;
          self.shopClerkList = res.data.shopClerkList;
          self.addressData = res.data.address;
        })
        .catch(error => {
          self.loading = false;
        });
    },

    /*发货*/
    onSubmit() {
      let self = this;
      let param = self.form;
      if (param.expressId == null) {
        ElMessage.error('请选择物流公司');
        return;
      }
      if (param.expressNo == '') {
        ElMessage.error('请填写物流单号');
        return;
      }
      let orderId = this.$route.query.orderId;
      OrderApi.delivery(
        {
          expressId: param.expressId,
          expressNo: param.expressNo,
          orderId: orderId
        },
        true
      )
        .then(data => {
          self.loading = false;
          ElMessage ({
            message: '恭喜你，发货成功',
            type: 'success'
          });
          self.getParams();
        })
        .catch(error => {
          self.loading = false;
        });
    },

    /*确认取消*/
    confirmCancel() {
      let self = this;
      let orderId = this.$route.query.orderId;
      let isCancel = self.forms.isCancel;
      OrderApi.confirm(
        {
          orderId: orderId,
          isCancel: isCancel
        },
        true
      )
        .then(data => {
          self.loading = false;
          ElMessage ({
            message: '恭喜你，审核成功',
            type: 'success'
          });
          self.getParams();
        })
        .catch(error => {
          self.loading = false;
        });
    },

    /*核销*/
    onExtract(e) {
      let self = this;
      let extractForm = self.extractForm;
      let param = {};
      param.orderId = e;
      param.extractStatus = extractForm.order.extractStatus;
      param.extractClerkId = self.extractClerkId;
      OrderApi.extract(param, true)
        .then(data => {
          self.loading = false;
          ElMessage ({
            message: '恭喜你，操作成功',
            type: 'success'
          });
          self.getParams();
        })
        .catch(error => {
          self.loading = false;
        });
    },

    /*虚拟商品发货*/
    onVirtual(e) {
      let self = this;
      let virtualForm = self.virtualForm;
      if (virtualForm.virtualContent == '') {
        ElMessage.error('请填写发货信息');
        return;
      }
      virtualForm.orderId = e;
      OrderApi.virtual(
        {
          orderId: virtualForm.orderId,
          virtualContent: virtualForm.virtualContent
        },
        true
      )
        .then(data => {
          self.loading = false;
          ElMessage ({
            message: '恭喜你，操作成功',
            type: 'success'
          });
          self.getParams();
        })
        .catch(error => {
          self.loading = false;
        });
    },

    closeAddress(e) {
      let self = this;
      if (e == false) {
        self.isChange = false;
        return false
      }
      let params = e.params;
      params.orderId = self.$route.query.orderId
      OrderApi.updateAddress(params,
          true
        )
        .then(data => {
          self.getParams();
          ElMessage ({
            message: '修改成功',
            type: 'success'
          });
        })
        .catch(error => {

        });
      self.isChange = false;
    },

    /*打开编辑*/
    editClick(item) {
      this.userModel = deepClone(item);
      this.open_edit = true;
    },

    /*关闭弹窗*/
    closeDialogFunc(e, f) {
      if (f == 'edit') {
        this.open_edit = e.openDialog;
        this.getParams();
      }
    },

    /*取消*/
    cancelFunc() {
      this.$router.back(-1);
    },

    changeAdd() {
      this.isChange = true;
    }
  }
};
</script>
<style></style>
