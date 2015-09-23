function FlyingWorkflowFormFace(configs) {
	/** 全局属性区 */
	this.configs = configs || {};// 配置属性
	
	this.pk = '';// 主键名称
	
	this.parent = null;//父对象
	
	this.params = {};//请求参数
	
	this.formArea = {};
	
	/** 初始化操作区 */
	this.Constructor = function() {// 构造方法
		var me = this;

		for ( var name in me.configs) {// 初始化属性
			me[name] = me.configs[name];
		}
		
		me.formArea = new Ext.Panel({
			region : 'center',
    		html : "<div id='loading'><div class='loading-indicator'><img src='img/loading32.gif' width='31' height='31'style='margin-right: 8px; float: left; vertical-align: top;' />数据加载中…… <br /></div></div>"+
    		"<iframe id='flowform' src='GetFormServlet?show=false&FLOW_ID="+me.params["FLOW_ID"]+"' frameborder='0' width='100%' height='100%'></iframe>"
		});
	};
	
	this.Constructor();
	
	this.init = function(){
		Ext.get('flowform').dom.onload = function() {
			Ext.get('loading').remove();
    	};
	};
	
	/** 表单按钮区 */
	this.btn_save = new Ext.Button({// 保存按钮
		text : '提交',
		iconCls : 'save',
		scope:this,
		handler : function(btn) {
			var me = this;
			
			var flowflow = Ext.get("flowform").dom;
			
			if (flowflow.document){//IE 
				flowflow.document.forms[0].submit(); 
			}else{//Firefox     
				flowflow.contentDocument.forms[0].submit();
			} 

			var task = new Ext.util.DelayedTask(function(){
				me.parent.afterSubmit();
			});
			
			task.delay(500);
			
			
		}
	});
	
	this.center = new Ext.Panel({
		layout : 'border',
		frame : true,
		border : false,
		buttonAlign : 'center',
		items : [this.formArea],
		buttons : [ this.btn_save ]
	});
}