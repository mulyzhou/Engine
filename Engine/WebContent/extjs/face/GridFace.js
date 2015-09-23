function GridFace(configs,parent) {
	/** 全局属性区 */
	this.configs = configs || {};// 配置属性
	
	this.parent = parent;//父亲对象
	
	this.cn = '';// 中文

	this.en = '';// 英文
	
	this.author = 'zdf';// 作者

	this.version = '1.0';// 版本
	
	this.params = {};//业务数据
	
	this.pk = '';// 主键名称
	
	this.pageSize = 10;// 分页
	
	this.cmArray = new Array();// 表格列数组
	
	this.modelArray = new Array();// 实体数组
	
	this.tableAction = ""; // 列表数据请求的url
	
	this.singleSelect =  false;//表格是否能多选
	
	this.gridRowClick = function() {//初始化表格行单击事件
		if(this.parent){
			Ext.fcache.obj = this.parent;
		}else{
			Ext.fcache.obj = this;
		}
	};
	
	this.gridDbRowClick = Ext.emptyFn;//初始化表格双击事件
	
	this.gridRender = Ext.emptyFn;//表格渲染事件
	
	/** 初始化操作区 */
	this.Constructor = function() {// 构造方法
		for ( var name in this.configs) {// 初始化属性
			if(!(this.parent != null && typeof this.configs[name] == "function")){
				this[name] = this.configs[name];
			}
		}
		
		if(this.columns){
			if (this.tableAction == "") {// 列表默认值
				this.tableAction = "common.action?command=" + this.en + ".selectAll";
			}
			
			this.cmArray = Ext.futil.handleColumn(this.columns,this);
			
			this.modelArray = Ext.futil.handleModel(this.columns);
			
			for(var i = 0;i < this.cmArray.length;i++){
				if(this.cmArray[i]["isPk"]){
					this.pk = this.cmArray[i]["dataIndex"];
					break;
				}
			}
		}
	};
	
	this.Constructor(); // 执行构造方法，初始化对象
	
	/** grid构建区域 */
	this.Model = Ext.data.Record.create(this.modelArray);// 用户对象

	this.ds = new Ext.data.Store({// 数据源
		autoLoad : true,
		remoteSort : true,
		baseParams:this.params,
		proxy : new Ext.data.HttpProxy({
			url : this.tableAction
		}),
		reader : new Ext.data.JsonReader({
			root : 'data',
			totalProperty : "total"
		}, this.Model)
	});

	this.pagingBar = new Ext.PagingToolbar({// 分页工具栏
		pageSize : this.pageSize,
		store : this.ds,
		displayInfo : false
	});

	this.grid = new Ext.grid.FlyingGridPanel({// 列表
		iconCls : 'icon-grid',
		margins : '2 2 2 2',
		loadMask : {
			msg : '数据加载中...'
		},
		cm : this.cmArray,
		ds : this.ds,
		bbar : this.pagingBar,
		sm : new Ext.grid.CheckboxSelectionModel({
			singleSelect : this.singleSelect
		}),
		enableColumnMove : false,
		trackMouseOver : false,
		listeners:{
			scope:this,
			rowclick:this.gridRowClick,
			rowdblclick:this.gridDbRowClick,
			render : this.gridRender
		}
	});
	
	/** 容器 */
	this.center = new Ext.Panel({
		layout : 'fit',
		border : false,
		items : [ this.grid ]
	});
}