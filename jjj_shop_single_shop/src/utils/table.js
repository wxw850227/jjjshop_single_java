/*
 * 合并行
 */
export const mergeTable = (list) => {
	let curItem = {
	  isFirst: false,
	  index: 0,
	  rowSpan: 1
	};
	for (let i = 0; i < list.length; i++) {
	  let item = list[i];
	  item.rowSpan = null;
	  if (!curItem.isFirst) {
	    curItem.isFirst = true;
	    curItem.index = i;
	    curItem.productId = item.productId;
	  } else {
	    if (curItem.productId != item.productId) {
	      list[curItem.index].rowSpan = curItem.rowSpan;
	      curItem.rowSpan = 1;
	      curItem.index = i;
	      curItem.productId = item.productId;
	    } else {
	      curItem.rowSpan++;
	    }
	  }
	}
	list[curItem.index].rowSpan = curItem.rowSpan;
	return list;
}
