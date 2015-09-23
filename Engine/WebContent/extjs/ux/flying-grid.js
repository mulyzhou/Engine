/**
 * 权限Grid标签
 * @author zjx 2011-10-25
 * @param security 数据列是否要进行权限验证
 * @param securityname 数据列权限验证的名称
 */
Ext.grid.FlyingGridPanel = Ext.extend(Ext.grid.GridPanel, {
    initComponent : function() {
    	this.initFn();
    	Ext.grid.FlyingGridPanel.superclass.initComponent.call(this);
    },
    initFn:function(){
    	//判断传进来的cm数组中是否都有权限，如果没有，从cm数组中删除
    	var securitycm = [];
    	securitycm.push(new Ext.grid.CheckboxSelectionModel());	//添加复选框列
    	for(var i = 0;i<this.cm.length;i++){
    		var item = this.cm[i];
    		item.security = item.security || false;
    		if(!Ext.fcache.debug && item.security == true){	//是权限验证的列
    			if(item.securityname==null){	//需要进行验证的控件，必须指定securityname属性
					alert("button缺少securityname属性！");
					item.securityname = '';
				}
        		var check = Ext.fcache.checkSecurity(item.securityname,2);
        		if(check){	//如果权限通过验证，则加入显示列的数组中
        			securitycm.push(item);
        		}
    		}else{
    			securitycm.push(item);
    		}
    	}
    	this.cm = new Ext.grid.ColumnModel(securitycm);
    }
});
