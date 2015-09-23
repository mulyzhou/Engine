function ToolbarFace(configs,parent) {
	/** 全局属性区 */
	this.configs = configs || {};// 配置属性	
	
	this.parent = parent;//父亲对象
	
	this.buttonArray = new Array();// 按钮数组
	
	this.isSearch = true;// 是否需要查询功能

	this.searchArray = new Array();// 查询数据的数组
	
	/** 初始化操作区 */
	this.Constructor = function() {// 构造方法

		for ( var name in this.configs) {// 初始化属性
			if(!(this.parent != null && typeof this.configs[name] == "function")){
				this[name] = this.configs[name];
			}
		}
		
		this.buttonArray = Ext.futil.handleButton(this.btns,this);
		
		this.handleSearch();
	};
	
	this.handleSearch = function(){
		var me = this;
		
		var allSearchField = "";
		
		me.columns = me.columns || new Array();
		
		for ( var m = 0; m < me.columns.length; m++) {// 构建表格列
			if(me.columns[m]["isColumn"] == false){
				continue;
			}
			if(me.columns[m]["hidden"] != undefined && me.columns[m]["hidden"]==true){
				continue;
			}
			if(me.columns[m]["isSearch"] != undefined && me.columns[m]["isSearch"]==false){
				continue;
			}
			if(me.columns[m]["renderer"] != undefined){
				continue;
			}
			if(!me.columns[m]["isPk"]){
				allSearchField += me.columns[m].dataIndex + "-";//全部查询
			}
		}
		
		var allSearchItem = new Array();
		allSearchItem.push("全部");
		allSearchItem.push(allSearchField);
		
		me.searchArray.push(allSearchItem);
		me.combo_search.setValue(allSearchField);
		
		for ( var i = 0; i < me.columns.length; i++) {//构建查询项		
			if (me.columns[i].isSearch != undefined && me.columns[i].isSearch) {// 是否填入查询控制
				var item = new Array();
				item.push(me.columns[i].header);
				item.push(me.columns[i].dataIndex);
				me.searchArray.push(item);
			}
		}
		
		if (me.isSearch) {// 判断是否添加查询控件
			me.buttonArray.push("->");
			me.store_search.loadData(me.searchArray);
			me.buttonArray.push(me.combo_search);
			me.buttonArray.push(me.txt_search);
		}
	};
	
	this.clickAdd = function(b,e){
		if(this.parent){
			this.parent.clickAdd(b,e);
		}
	};
	
	this.clickModify = function(b,e){
		if(this.parent){
			this.parent.clickModify(b,e);
		}
	};
	
	this.clickDelete = function(b,e){
		if(this.parent ){
			this.parent.clickDelete(b,e);
		}
	};
	
	this.clickDownload = function(b,e){
		if(this.parent){
			this.parent.clickDownload(b,e);
		}
	};
	
	this.clickRefresh = function(b,e){
		if(this.parent){
			this.parent.refresh(b,e);
		}
	};
	
	this.clickShow = function(b,e){
		this.clickShow(b,e);
	}; 
	
	this.store_search = new Ext.data.SimpleStore({//查询数据集合
		fields : [ 'field', 'fieldValue' ],
		data : []
	});
	
	this.combo_search = new Ext.form.ComboBox({//查询下来列表
		xtype : 'combo',
		store : this.store_search,
		displayField : 'field',
		valueField : 'fieldValue',
		editable : false,
		triggerAction : 'all',
		mode : 'local'
	});
	
	this.txt_search = new Ext.form.TextField({//查询文本框
		enableKeyEvents : true,
		listeners : {
			scope : this,
			//keypress : function(txt,e){
			keyup : function(txt,e){
				//if(e.getKey() == e.ENTER){
				var param = {"searchField" : this.combo_search.getValue(),"searchValue":this.txt_search.getValue()}
				this.parent.clickSearch(param);
				//}
			}
		}
	});
	
	this.Constructor();
	
	this.toolbar = new Ext.FlyingToolbar({// 操作工具栏
		items : this.buttonArray
	});
	
	/** 容器 */
	this.center = new Ext.Panel({
		height : 27,
		layout : 'fit',
		border : false,
		items : [ this.toolbar ]
	});
}