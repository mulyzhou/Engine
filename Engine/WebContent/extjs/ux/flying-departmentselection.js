/**
 * 选部门组件
 * 包括一个文本控件，一个隐藏域，一个按钮
 * 控件初始化时，Ajax调用数据，文件控件setValue时，重新构建数据，将人员Id字符串变换成人员Name字符串，隐藏域进行赋值。
 * @author zdf 2013-5-23
 */
Ext.namespace("Ext.flying");

Ext.flying.DepartmentSelection = Ext.extend(Ext.form.TextField,{
	data:{},		//后台请求的数据
	submitName:'',	//提交表单的名称
	input:null,	    //隐藏域
	departmentId:'',//选中树节点的Id
	names:'',		//对外接口，选中的Name字符串
	values:'',		//对外接口,选中的Id字符串
	setValue : function(v){ //初始化完成后，才执行setValue方法. 此处不用赋值
		var me = this;
		//根据id显示相应的name
		var id,name;
		var listdata = me.data.listdata;
		for(var i=0;i<listdata.length;i++){
			var departmentId = listdata[i]["ZZJGDM"];
			if(v==departmentId){
				id = departmentId;
				name = listdata[i]["DWMC"];
				break;
			}
		}
		me.values = id;
		me.names = name;
		//同步数据到隐藏域
		me.input.value = me.values ;
		
		Ext.flying.DepartmentSelection.superclass.setValue.call(this,me.names);
	},
	tree:null,  	//部门树
	win:null,		//弹出窗口
	clickFn:function(){
		var me = this;
		
		//1 请求树数据
	    Ext.Ajax.request({
	        url : 'common.action?command=ZZJGXX.selectAllList',
	        method : 'POST',
	        async : false,
	        success: function(response, opts){
	                var responseJson = Ext.util.JSON.decode(response.responseText);
	                me.data = responseJson;
	                if(responseJson.success){

	                }else{
	                    Ext.Msg.show({
	                            title : '异常提示',
	                            msg : '获取数据出错',
	                            buttons : Ext.Msg.OK,
	                            icon : Ext.Msg.ERROR
	                    });
	                }
	        }
	    });
	    
		var treedata = {
			"id" : "0",
			"text" : "根",
			"orderId" : "1",
			"expanded" : true,
			"children" : me.data.data
		};
		
		me.tree = new Ext.tree.TreePanel({
	    	region : "center",
			border : false,
			rootVisible : false, //是否显示根元素
			autoScroll : true,
			root : treedata,
			buttons:[{
				text:'确定',
				handler:function(){
					//将值设置到表单中
					me.setValue(me.departmentId);
					
					me.win.hide();
				}
			},{
				text:'关闭',
				handler:function(){
					me.win.hide();
				}
			}],
			listeners : {
				'click' : function(n) {
					var sn = this.selModel.selNode || {};
					if (n.id != sn.id) {
						me.departmentId = n.id;
					}
				}
			}
	    });
		me.win = new Ext.Window({
			title : "选择部门",
			layout : 'fit',
			width : 500,
			height : 300,
			closeAction : 'hide',
			plain : true,
			items : [me.tree]
		});
		
		me.win.show();
	},
	
	initComponent : function() {
		var me = this;
		//更改输入框的name
		me.submitName = me.name;
		me.name = "NAME_"+me.name;
		
		Ext.flying.DepartmentSelection.superclass.initComponent.call(this);
		
		//1 请求树数据
	    Ext.Ajax.request({
	        url : 'common.action?command=ZZJGXX.selectAllList',
	        method : 'POST',
	        async : false,
	        success: function(response, opts){
	                var responseJson = Ext.util.JSON.decode(response.responseText);
	                me.data = responseJson;
	                if(responseJson.success){

	                }else{
	                    Ext.Msg.show({
	                            title : '异常提示',
	                            msg : '获取数据出错',
	                            buttons : Ext.Msg.OK,
	                            icon : Ext.Msg.ERROR
	                    });
	                }
	        }
	    });
	},
	onRender : function(ct, position) {
		Ext.flying.DepartmentSelection.superclass.onRender.call(this, ct, position);

		var me = this;
		//隐藏域
		me.template =  new Ext.Template('<input type="hidden" name="{0}" value="" />');	//隐藏域

		var input;
		var targs = [me.submitName];

		input = me.template.append(ct, targs, false);	//false返回htmlElement
		
		me.input = input;
		
		//绑定事件
		me.on({
			"focus":me.clickFn,
			scope:me
		});
	}
});
	
Ext.reg('departmentselection',Ext.flying.DepartmentSelection);