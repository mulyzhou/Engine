function EmailGridFace(configs) {
	/** 全局属性区 */
	this.configs = configs || {};// 配置属性

	this.gridObj = null;//grid对象
	
	this.formObj = null;//form对象
	
	this.toolbarObj = null;//toolbar对象
	
	this.buttonArray = new Array();// 操作数组
	
	this.params = {};//业务数据
	
	this.pk = '';// 主键名称
	
	this.opSqlid = '';//添加，修改自定义sqlid
	/** 生命周期中的方法*/
	this.beforeAddShow = Ext.emptyFn;//添加页面显示之前方法
	
	this.afterAddShow = function(){//添加页面显示之后方法
		Ext.fFaceUtil.afterFormShow(this);
	};
	
	this.beforeAddSave = Ext.emptyFn;//添加保存之前方法
	
	this.afterAddSave = Ext.emptyFn;//添加保存之后方法
	
	this.beforeModifyShow = Ext.emptyFn;//修改页面显示之前方法
	
	this.afterModifyShow = function(){//修改页面显示之后方法
		Ext.fFaceUtil.afterFormShow(this);
	};
	
	this.beforeModifySave = Ext.emptyFn;//修改保存之前方法
	
	this.afterModifySave = Ext.emptyFn;//修改保存之后方法
	
	this.beforeDeleteSave = Ext.emptyFn;//删除保存之前方法 
	
	this.afterDeleteSave = Ext.emptyFn;//删除保存之后方法
	
	/** 初始化操作区 */
	this.Constructor = function() {// 构造方法

		for ( var name in this.configs) {// 初始化属性
			this[name] = this.configs[name];
		}
		
		//初始化grid
		this.gridObj = new GridFace(this.configs,this);
		this.gridObj.center.region = "center";
		this.grid = this.gridObj.grid;
		this.ds = this.gridObj.ds;
		this.pk = this.gridObj.pk;
		
		//初始化表单
		this.formObj = new FormFace(this.configs,this);
		this.form = this.formObj.form;
		
		//初始化工具栏
		this.toolbarObj = new ToolbarFace(this.configs,this);
		this.toolbarObj.center.region = "north";
		this.toolbar = this.toolbarObj.toolbar;
	};
	
	this.Constructor(); // 执行构造方法，初始化对象
	
	this.reset = Ext.emptyFn;//初始化方法
	
	/** 默认动作处理区 */
	this.clickAdd = function(b,e) {//默认添加
		this.opType = "add";
		
		if(b.sqlid){
			this.opSqlid = b.sqlid;
		}
		
		this.params = {};
		this.form.getForm().reset();//表单清空
		
		if (this.beforeAddShow() == false){
			return;
		}
		
		this.formPanel.enable();
		this.formPanel.setTitle("添加——"+this.cn);
		this.center.add(this.formPanel);
		this.center.setActiveTab(this.formPanel);
		this.center.doLayout();
		
		this.afterAddShow();
	};
	
	this.clickModify = function(b,e) {
		
		if(Ext.fFaceUtil.clickModify(this)){
			if(b.sqlid){
				this.opSqlid = b.sqlid;
			}
			
			this.afterModifyShow();
		}
	};
	
	this.formInit = function(){
		this.formPanel.enable();
		this.formPanel.setTitle("修改——"+this.cn);
		this.center.add(this.formPanel);
		this.center.setActiveTab(this.formPanel);
		
		this.center.doLayout();
	};
	
	this.clickDelete = function(b,e){//默认删除
		
		if(this.opType == "modify"){
			this.center.remove(this.formPanel,false);//关闭修改页面
		}
		
		this.batchDeleteUrl = "common.action?command=init.batchDelete";
		
		if(b.sqlid){
			this.batchDeleteUrl += "&SQLID=" + b.sqlid + "&PK=" + this.pk;
		}else{
			this.batchDeleteUrl += "&SQLID=" + this.en + ".delete&PK=" + this.pk;
		}
		
		Ext.fFaceUtil.clickDelete(this);
	};
	
	this.clickDownload = function(b,e){//默认删除
		Ext.fFaceUtil.clickDownload(this);
	};
	
	this.refresh = function(b,e){//刷新方法
		this.ds.reload();
		
		this.reset();
	};
	
	this.clickSearch = function(param){//默认搜索
		for ( var name in param) {// 初始化属性
			this.grid.getStore().baseParams[name] = param[name];
		}
		this.grid.getStore().reload();
	};
	
	this.clickShow = function(b,e) {//默认显示片段

		if(typeof b["beforeRun"] == "function"){//方法前拦截
			var beforeResult = b["beforeRun"]();
			
			if(beforeResult){
				return;
			}
		}else if(typeof b["beforeRun"] == "string"){
			var beforeResult = this[b["beforeRun"]]();
			
			if(beforeResult){
				return;
			}
		}
		
		if(this[b["linkName"]] && this[b["linkName"]]["center"])
		{
			this[b["linkName"]]["center"].closable = true;
			this.center.add(this[b["linkName"]]["center"]);
			this.center.setActiveTab(this[b["linkName"]]["center"]);
		}
		
		this.center.doLayout();
		
		if(typeof b["afterRun"] == "function"){//方法前拦截
			b["afterRun"]();
		}else if(typeof b["afterRun"] == "string"){
			this[b["afterRun"]]();
		}
	};
	
	this.clickSave = function(b,e) {//默认保存
		Ext.fFaceUtil.clickSave(this);
	};
	
	this.clickCancel = function(b,e){//默认取消
		this.center.remove(this.formPanel,false);
		
		this.center.doLayout();
	};
	
	/** 容器 */
	this.formPanel = new Ext.Panel({
		title : this.cn,
		layout : 'fit',
		closable : true,
		autoScroll : true,
		items : [ this.form ]
	});
	
	/** 列表 */
	this.listPanel = new Ext.Panel({
		title : this.cn,
		layout : 'border',
		items : [ this.toolbarObj.center,this.gridObj.center ]
	});
	
	/** 容器 */
	this.center = new Ext.FlyingTabPanel({//tabs
    	resizeTabs : true,
    	autoDestroy : false,//关闭panel时，不销毁panel
    	activeTab : 0,
    	items:[this.listPanel],
    	listeners:{
    		'remove':function(ct,component) {
    	    	var dom = component.el.dom;
    	    	if(dom.parentNode){
    		    	dom.parentNode.removeChild(dom);//手动删除ext遗留的dom节点   extbug
    	    	}
     		return true; 
    	 }
    	}
    });
}