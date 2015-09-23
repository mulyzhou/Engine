function HdAuditListFace(configs) {
	/** 全局属性区 */
	this.configs = configs || {};// 配置属性

	this.cn = '';// 中文

	this.en = '';// 英文
	
	this.fileName = '';//文件名称
	
	this.face = '';//文件模板
	
	this.pk = '';// 主键名称

	this.author = 'zdf';// 作者

	this.version = '1.2';// 版本
	
	this.params = {};//业务数据
	
	this.pageSize = 10;// 分页

	this.opType = '';// 用以区分增加，修改类型

	this.tableAction = ""; // 列表数据请求的url
	
	this.addUrl = "";//添加请求的url
	
	this.modifyUrl = "";//修改请求的url
	
	this.deleteUrl = "";//删除请求的url

	this.formColumn = 2;//表单列数
	
	this.formFileUpload = false;//表单是否含有上传控件
	
	this.leftArray = new Array();// 表单左侧数组

	this.rightArray = new Array();// 表单右侧数组

	this.formArray = new Array();// 表单数组
	
	this.modelArray = new Array();// 实体数组

	this.buttonArray = new Array();// 操作数组

	this.isSearch = false;// 是否需要查询功能

	this.searchArray = new Array();// 查询数据的数组

	this.cmArray = new Array();// 表格列数组
	
	/** 生命周期中的方法*/
	this.beforeAddShow = Ext.emptyFn;//添加页面显示之前方法
	
	this.afterAddShow = function(){//添加页面显示之后方法
		this.kindeditorInit();
	};
	
	this.beforeAddSave = Ext.emptyFn;//添加保存之前方法
	
	this.afterAddSave = Ext.emptyFn;//添加保存之后方法
	
	this.beforeModifyShow = Ext.emptyFn;//修改页面显示之前方法
	
	this.afterModifyShow = function(){//修改页面显示之后方法
		this.kindeditorInit();
	};
	
	this.beforeModifySave = Ext.emptyFn;//修改保存之前方法
	
	this.afterModifySave = Ext.emptyFn;//修改保存之后方法
	
	this.beforeDeleteSave = Ext.emptyFn;//删除保存之前方法
	
	this.afterDeleteSave = Ext.emptyFn;//删除保存之后方法
	
	this.reset = function(){//初始化方法
		try{
			this.params = {};
			this.formWindow.hide();
		}catch(e){
			console.log(e);
		}
	};

	this.addSuccess = function() {//添加成功方法
		var me = this;
		
		me.form.getForm().reset();//表单清空
	};
	
	this.gridRowClick = function() {//初始化表格行单击事件
		Ext.fcache.obj = this;
	};
	
	this.gridDbRowClick = Ext.emptyFn;//初始化表格双击事件
	
	this.gridRender = Ext.emptyFn;//表格渲染事件
	
	/** 初始化操作区 */
	this.Constructor = function() {// 构造方法
		var me = this;

		for ( var name in me.configs) {// 初始化属性
			me[name] = me.configs[name];
		}

		me.handleButton();// 处理按钮
		me.handleColumn();// 处理列
		me.handleForm();// 处理表单

		if (me.tableAction == "") {// 列表默认值
			me.tableAction = "common.action?command=" + me.en + ".selectAll";
		}

		if (me.isSearch) {// 判断是否添加查询控件
			me.buttonArray.push("->");
			me.store_search.loadData(this.searchArray);
			me.buttonArray.push(me.combo_search);
			me.buttonArray.push(me.txt_search);
		}
		
		me.buttonArray.push("->");;
		me.buttonArray.push(me.typeCombo_search);
	};

	this.handleColumn = function() {// 列处理方法
		var me = this;

		var field = {};
		var allSearchField = "";
		
		me.columns = me.columns || new Array();
		
		for ( var m = 0; m < me.columns.length; m++) {// 构建表格列
			if(me.columns[m]["isColumn"] == false){
				continue;
			}
			me.cmArray.push(me.columnPack(me.columns[m]));// 包装表格列
			if(!me.columns[m]["isPk"]){
				allSearchField += me.columns[m].dataIndex + "-";//全部查询
			}
		}
		
		
		var allSearchItem = new Array();
		allSearchItem.push("全部");
		allSearchItem.push(allSearchField);
		
		me.searchArray.push(allSearchItem);
		me.combo_search.setValue(allSearchField);
		
		for ( var i = 0; i < me.columns.length; i++) {//构建查询项，实体对象
			if (field[me.columns[i].dataIndex] == undefined) {// 构建实体
				field[me.columns[i].dataIndex] = me.columns[i].dataIndex;
				me.modelArray.push(me.modelPack(me.columns[i]));// 包装实体对象
			}
			
			if (me.columns[i].isSearch != undefined && me.columns[i].isSearch) {// 是否填入查询控制
				var item = new Array();
				item.push(me.columns[i].header);
				item.push(me.columns[i].dataIndex);
				me.searchArray.push(item);
			}
		}
	};
	
	/** 列项包装方法*/
	this.columnPack = function(columnObj) {
		var me = this;

		var columnTemplate = {// 默认值
			sortable : true,// 可排序
			menuDisabled : true,// 无需表头菜单
			width : 100
		// 默认宽度100
		};
		me.assign(columnTemplate, columnObj);// 进行赋值操作

		if (columnTemplate["isPk"]) {// 对主键进行处理
			me.pk = columnTemplate.dataIndex;
			columnTemplate["hidden"] = true;// 隐藏主键列
		}

		if (columnTemplate["renderer"]) {// 处理列自定义的问题
			columnTemplate["renderer"] = me[columnTemplate["renderer"]];
		}

		return columnTemplate;
	};
	
	/** 对象包装方法*/
	this.modelPack = function(columnObj) {
		var model = {};
		model.name = columnObj.dataIndex || '';// 名称
		model.type = columnObj.type || 'string';// 类型

		return model;
	};
	
	/** 创建操作按钮方法*/
	this.handleButton = function() {// 操作处理方法
		var me = this;

		me.btns = me.btns || new Array();
		for ( var i = 0; i < me.btns.length; i++) {// 遍历
			me.buttonArray.push(me.buttonPack(me.btns[i]));
		}
	};
	
	/** 操作按钮包装方法*/
	this.buttonPack = function(buttonObj) {
		var me = this;
		var buttonTemplate = {};//按钮模板
		/** 如果是一个外部文件，先加载外部文件 */
		if(buttonObj["link"] != null){
			var src = buttonObj["link"];
			var type = buttonObj["type"] || "common";
			me[buttonObj["linkName"]] = Ext.fcache.push(src,type);
			me[buttonObj["linkName"]].parent = me;
		}

		/** 操作预定义类型处理 */
		if (buttonObj["btype"] == "add") {
			me.assign(buttonTemplate, me.btn_add);
			me.addUrl = buttonObj["url"] || "";
			me.addLinkName = buttonObj["linkName"] || "";
		} else if (buttonObj["btype"] == "modify") {
			me.assign(buttonTemplate, me.btn_modify);
			me.modifyUrl = buttonObj["url"] || "";
		} else if (buttonObj["btype"] == "delete") {
			me.assign(buttonTemplate, me.btn_delete);
			me.deleteUrl = buttonObj["url"] || "";
		} else if (buttonObj["btype"] == "refresh") {
			me.assign(buttonTemplate, me.btn_refresh);
		} else if (buttonObj["btype"] == "download") {
			me.assign(buttonTemplate, me.btn_download);
		}else{
			if(buttonObj["link"] != null && buttonObj["handler"] == null){
				buttonObj["handler"] = me.clickShow;
			}
		}
		
		/** 更改操作函数的作用域，换做当前对象 */
		if (buttonObj["handler"]) {
			buttonObj["handler"] = buttonObj["handler"].createDelegate(me);
		}
		
		me.assign(buttonTemplate, buttonObj);
		
		return buttonTemplate;// 返回重构的btn
	};

	/** 构建表单方法*/
	this.handleForm = function() {// 处理表单方法
		var me = this;

		for ( var i = 0; i < me.columns.length; i++) {// 遍历
			if (me.columns[i]["isPk"]) {// 对主键进行处理
				continue;
			}
			
			if (me.columns[i]["isForm"] == false) {// 不做为表单项
				continue;
			}
			/** 按列显示 */
			if(me.formColumn == 1){//分成一列显示
				me.formArray.push(me.formPack(me.columns[i]));
				
			}else if(me.formColumn == 2){//分成两列显示
				var order = me.columns[i]["order"];
				if(order == null){
					if (i % 2 == 1) {// 奇数放左侧
						me.leftArray.push(me.formPack(me.columns[i]));
					} else {// 偶数放右侧
						me.rightArray.push(me.formPack(me.columns[i]));
					}
				}else{
					if (order % 2 == 1) {// 奇数放左侧
						me.leftArray.push(me.formPack(me.columns[i]));
					} else {// 偶数放右侧
						me.rightArray.push(me.formPack(me.columns[i]));
					}
				}
			}
		}
		
		/** 进行排序显示*/
		if(me.formColumn == 1){//分成一列显示
			me.formArray.sort(me.sortOrder);//排序
			
		}else if(me.formColumn == 2){//分成两列显示
			me.leftArray.sort(me.sortOrder);//左侧排序
			me.rightArray.sort(me.sortOrder);//右侧排序
		}
	};
	
	/** 排序算法*/
	this.sortOrder = function sortNumber(first, second)
	{
	  return first.order - second.order;
	};
	
	/** 表单对象包装方法*/
	this.formPack = function(formObj) {// 表单项包装方法
		var me = this;
		var formTemplate = {};// 默认值
		var packFormObj = {};

		if (formObj["xtype"] != undefined) {// 是否可以为空
			packFormObj["xtype"] = formObj["xtype"];
		}
		
		packFormObj["fieldLabel"] = formObj["header"];// 标题
		packFormObj["name"] = formObj["dataIndex"];// 名称

		if (formObj["allowBlank"] != undefined) {// 是否可以为空
			packFormObj["allowBlank"] = formObj["allowBlank"];
		}
		if (formObj["maxLength"] != undefined) {// 最大长度
			packFormObj["maxLength"] = formObj["maxLength"];
		}
		
		if (formObj["readOnly"] != undefined) {//正则判断
			packFormObj["readOnly"] = formObj["readOnly"];
		}
		
		if (formObj["regex"] != undefined) {//正则判断
			packFormObj["regex"] = formObj["regex"];
		}
		
		if (formObj["regexText"] != undefined) {// 正则判断提示
			packFormObj["regexText"] = formObj["regexText"];
		}
		
		if (formObj["anchor"] != undefined) {//改变表单的宽度
			packFormObj["anchor"] = formObj["anchor"];
		}
		
		if (formObj["dateConfig"] != undefined) {// my97的配置属性
			packFormObj["dateConfig"] = formObj["dateConfig"];
		}
		
		if (formObj["items"] != undefined) {// 是否有子项
			packFormObj["items"] = formObj["items"];
		}
		
		if (formObj["inputType"] != undefined) {// 输入类型
			packFormObj["inputType"] = formObj["inputType"];
			if(formObj["inputType"] == "file"){
				me.formFileUpload = true;
			}
		}

		if (formObj["value"] != undefined) {// 默认值
			packFormObj["value"] = formObj["value"];
		}

		if (formObj["msgTarget"] != undefined) {// 提示显示的位置
			packFormObj["msgTarget"] = formObj["msgTarget"];
		}
		
		/** 更改操作函数的作用域，换做当前对象 */
		if (formObj["validator"] != undefined) {//验证操作
			packFormObj["validator"] = me[formObj["validator"]].createDelegate(me);
		}
		
		if (formObj["order"] != undefined) {// 排序
			packFormObj["order"] = formObj["order"];
		}else{
			packFormObj["order"] = 9999;
		}		
		
		/** 根据不同类型构建不同的表单控件 */
		if (formObj["xtype"] == "combo") {// 如果是下拉列表框类型
			formTemplate = me.comboPack(formObj);
		} else if (formObj["xtype"] == "datefield") {
			formTemplate = me.datePack(formObj);
		} else if(formObj["xtype"] == "textarea"){
			formTemplate = me.textAreaPack(formObj);
		} else {
			// 对于类型的处理的预留区
		}

		me.assign(formTemplate, packFormObj);// 进行赋值操作

		return formTemplate;
	};
	
	/**下来列表项包装方法*/
	this.comboPack = function(formObj) {
		var comboTemplate = {};

		if (formObj["url"] == undefined) {
			comboTemplate = {
				xtype : 'combo',
				store : new Ext.data.SimpleStore(
						{
							fields : [ formObj["displayField"],
									formObj["valueField"] ],
							data : formObj["data"]
						}),
				displayField : formObj["displayField"],
				valueField : formObj["valueField"],
				typeAhead : true,
				mode : 'local',
				value : 0,
				triggerAction : 'all',
				fieldLabel : formObj["header"],
				hiddenName : formObj["dataIndex"],
				allowBlank : false,
				maxLength : 50
			};
		} else {
			comboTemplate = {
				xtype : 'combo',
				fieldLabel : formObj["header"],
				hiddenName : formObj["dataIndex"],
				valueField : formObj["valueField"],
				displayField : formObj["displayField"],
				mode : 'remote',
				pageSize : 10,
				minChars : 1,
				store : new Ext.data.Store({
					autoLoad : true,
					url : formObj["url"],
					reader : new Ext.data.JsonReader({
						root : 'data',
						totalProperty : "total"
					}, [ {
						name : formObj["displayField"]
					}, {
						name : formObj["valueField"]
					} ])
				}),
				selectOnFocus : true,
				triggerAction : 'all',
				loadingText : '加载中...'
			};
		}

		return comboTemplate;
	};

	/**时间包装方法*/
	this.datePack = function(formObj) {
		var dateTemplate = {
			xtype : 'datefield',
			fieldLabel : formObj["header"],
			format : formObj["format"] == undefined ? "Y-m-d"
					: formObj["format"],
			name : formObj["dataIndex"]
		};

		return dateTemplate;
	};
	
	/**文本框包装方法*/
	this.textAreaPack = function(formObj){
		var textAreaTemplate = {
				xtype : 'textarea',
				fieldLabel : formObj["header"],
				name : formObj["dataIndex"]
			};

			return textAreaTemplate;
	};
	
	/**属性置换方法*/
	this.assign = function(target, source) {// 赋值，将source中的所有属性赋值到target对象中
		source = source || {};
		for ( var name in source) {// 初始化属性
			target[name] = source[name];
		}
	};

	/** 默认动作处理区 */
	this.clickAdd = function() {
		var me = this;
		
		me.opType = "add";
		me.params = {};
		me.form.getForm().reset();//表单清空
		
		if (me.beforeAddShow() == false){
			return;
		}
		
		me.formWindow.show();
		me.afterAddShow();
	};

	
	this.clickModify = function() {
		var me = this;
		
		if (me.beforeModifyShow() == false){
			return;
		}
		
		me.opType = "modify";		
		var row = me.grid.getSelectionModel().getSelected();
		if(row != null){
			me.formWindow.show();//显示修改页面
			
			me.params[me.pk] = row.data[me.pk];//获取主键值

			for(var i =0;i<me.columns.length;i++){//遍历表单赋值
				if (me.columns[i]["isPk"]) {// 对主键进行处理
					continue;
				}
				var name = me.columns[i].dataIndex;
				if(name == undefined || me.form.getForm().findField(name) == undefined){
					continue;
				}
				me.form.getForm().findField(name).setValue(row.data[name]);
			}
			
			me.afterModifyShow();
		}else{
			Ext.Msg.show({
				title : '友情提示',
				msg : '请选择一条记录！',
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.WARNING
			});
		}
	};
	
	this.clickShow = function(button,event) {
		var me = this;

		if(typeof button["beforeRun"] == "function"){//方法前拦截
			var beforeResult = button["beforeRun"]();
			
			if(beforeResult){
				return;
			}
		}else if(typeof button["beforeRun"] == "string"){
			var beforeResult = me[button["beforeRun"]]();
			
			if(beforeResult){
				return;
			}
		}
		
		if(me[button["linkName"]] && me[button["linkName"]]["center"])
		{
			me[button["linkName"]+"_win"] = new Ext.Window({
	        	minimizable: true,
	            maximizable: true,
	        	width: 500,
	            height: 300,
	            layout: 'fit',
	            items: me[button["linkName"]]["center"]
			});
		}
		
		me[button["linkName"]+"_win"].show();
		
		if(typeof button["afterRun"] == "function"){//方法前拦截
			button["afterRun"]();
		}else if(typeof button["afterRun"] == "string"){
			me[button["afterRun"]]();
		}
	};

	this.kindeditorSync = function() {
		var me = this;

		for ( var i = 0; i < me.columns.length; i++) {// 遍历
			if (me.columns[i]["isPk"]) {// 对主键进行处理
				continue;
			}
			
			if (me.columns[i]["isForm"] == false) {// 不做为表单项
				continue;
			}
			
			if(me.columns[i]["xtype"] == "kindeditor"){
				me.form.getForm().findField(me.columns[i]["dataIndex"]).editor.sync();
			}
		}
	};
	
	this.kindeditorInit = function(){
		var me = this;

		for ( var i = 0; i < me.columns.length; i++) {// 遍历
			if (me.columns[i]["isPk"]) {// 对主键进行处理
				continue;
			}
			
			if (me.columns[i]["isForm"] == false) {// 不做为表单项
				continue;
			}
			
			if(me.columns[i]["xtype"] == "kindeditor" && me.form.getForm().findField(me.columns[i]["dataIndex"])){
				me.form.getForm().findField(me.columns[i]["dataIndex"]).flyingInit();
			}
		}
	};
	/** 增删改查默认区 */
	this.btn_add = {// 用户添加按钮
		text : '添加',
		iconCls : 'user-add',
		scope:this,
		handler : this.clickAdd
	};

	this.btn_modify = {// 用户修改按钮
			text : '修改',
			iconCls : 'user-edit',
			scope:this,
			handler : this.clickModify
		};
	
	this.btn_delete = {// 用户删除按钮
		text : '删除',
		iconCls : 'user-delete',
		scope:this,
		handler : function(b,e) {			
			var me = this;
			
			if (me.beforeDeleteSave() == false){//删除保存前操作
				return;
			}
			
			if(me.deleteUrl == ""){
				me.deleteUrl = "common.action?command="+me.en + ".delete";
			}
			
			var pk = me.pk;// 主键名称
			var row = me.grid.getSelectionModel().getSelected();//获取选中列			
			
			if (row) {
				Ext.Msg.confirm(b.text, '确定'+b.text+'?', function(btn) {
					if (btn == 'yes') {
						me.params[pk] = row.data[pk];//赋值

						if(me.opType == "modify"){
							me.formWindow.hide();//关闭修改页面
						}
						
						Ext.Ajax.request({
							url : me.deleteUrl,
							params : me.params,
							success : function(response, opts) {	
								
								Ext.fm.msg(me.cn,"恭喜，删除成功！");
								
								me.grid.getStore().remove(row);
								
								me.afterDeleteSave();//删除保存后操作
							},
							failure : function(response, opts) {
								var obj = Ext.util.JSON.decode(response.responseText);
								Ext.Msg.show({
									title : '异常提示',
									msg : obj.msg,
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
							}
						});
					}
				});
			}
		}
	};

	this.btn_refresh = {// 用户刷新按钮
		text : '刷新',
		iconCls : 'refresh',
		scope:this,
		handler : function() {
			var me = this;

			me.ds.reload();
			
			me.reset();
		}
	};

	this.btn_download = {// 用户下载按钮
		text : '导出',
		iconCls : 'download',
		scope:this,
		handler : function() {
			var me = this;
			var params = "&FILE_DOWNLOAD_NAME="+me.fileName;
			if(me.ds.sortInfo){
				params += "&dir="+me.ds.sortInfo.direction+"&sort="+me.ds.sortInfo.field;
			}
			if(me.ds.lastOptions && me.ds.lastOptions.params && me.ds.lastOptions.params.searchField ){
				params += "&searchField="+me.ds.lastOptions.params.searchField+"&searchValue="+me.ds.lastOptions.params.searchValue;
			}
			
			var exportForm = new Ext.FormPanel();
	    	exportForm.applyToMarkup(Ext.DomHelper.append(Ext.getBody(), {
				tag : "div"
			}, true));
	    	exportForm.getForm().getEl().dom.action = me.tableAction+params;  
	    	exportForm.getForm().getEl().dom.submit();

		}
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
					var me = this;
					me.ds.load({params:{'start':0,'limit':me.pageSize,'searchField':me.combo_search.getValue(),'searchValue':me.txt_search.getValue()}});
				//}
			}
		}
	});
	
	this.typeCombo_search = new Ext.form.ComboBox({//查询下来列表
		width : 250,
		valueField : "FLOW_ID",
		displayField : "FLOW_NAME",
		mode : 'remote',
		pageSize : 10,
		minChars : 1,
		store : new Ext.data.Store({
			autoLoad : true,
			url : "common.action?command=T_WF_INSTANCE.getFlowTypeByUserId&MODULE=hdplan",
			reader : new Ext.data.JsonReader({
				root : 'data',
				totalProperty : "total"
			}, [ {
				name : "FLOW_NAME"
			}, {
				name : "FLOW_ID"
			} ])
		}),
		selectOnFocus : true,
		triggerAction : 'all',
		loadingText : '加载中...',
		listeners : {
			scope : this,
			select : function(combo,row,i){
				var me = this;
				me.ds.load({params:{'start':0,'limit':me.pageSize,'searchField':"FLOW_ID",'searchValue':row.data["FLOW_ID"]}});
			}
		}
	});
	
	this.Constructor(); // 执行构造方法，初始化对象
	
	/** 页面跳转区 */
	this.dispatch = function(resultObject) {// type=1:显示表单；type=2:详情；type=3:审核。调整到表单申请页面
		// 从缓存中请求
		Ext.fcache.resultObject = resultObject;
		// 页面跳转
		Ext.fcache.changeUrl(resultObject.formName + ".js", "workflowform",
				null, false);
	};

	/** 表单按钮区 */
	this.btn_save = new Ext.Button(
			{// 保存按钮
				text : '保存',
				iconCls : 'save',
				scope:this,
				handler : function(btn) {
					var me = this;
					
					var url = "";
					if(me.addUrl == ""){//处理添加请求
						me.addUrl = "common.action?command="+me.en + ".insert";
					}
					
					if(me.modifyUrl == ""){//处理修改请求
						me.modifyUrl = "common.action?command="+me.en + ".update";
					}
					
					if(me.opType == 'add'){//如果是添加模式，请求地址
						if (me.beforeAddSave() == false){
							return;
						}
						url = me.addUrl;
					}else if(me.opType == 'modify'){//如果是修改模式，请求地址
						if (me.beforeModifySave() == false){
							return;
						}
						url = me.modifyUrl;
					}
					
					
					me.kindeditorSync();//对含有kindeditor控件进行处理
					
					if (me.form.getForm().isValid()) {
						me.form.getForm().submit({
											url : url,
											params : me.params,
											waitTitle : '请稍候',
											waitMsg : '正在提交表单数据,请稍候...',
											success : function(form, action) {
												if(!action.result.success){
													Ext.Msg.show({
														title : '异常提示',
														msg : action.result.msg,
														buttons : Ext.Msg.OK,
														icon : Ext.Msg.ERROR
													});
													return;
												}
												if (me.opType == 'add') {
//													if (me.ds.data.length <= me.pageSize) {
//														var model = new me.Model();// 声明对象
//														model.data[me.pk] = action.result.data;// 主键
//														for ( var i = 0; i < me.columns.length; i++) {// 遍历列对象
//															if (me.columns[i]["isPk"] || me.columns[i]["isForm"] == false) {// 对主键进行处理
//																continue;
//															}
//															var name = me.columns[i].dataIndex;
//															model.data[name] = form.findField(name).getValue();
//														}
//
//														me.ds.insert(0,[ model ]);
//
//														if (me.ds.data.length > me.pageSize) {
//															me.ds.remove(me.ds.getAt(me.ds.data.length - 1));
//														}
//														me.grid.getSelectionModel().selectRow(0);
//													}
													Ext.fm.msg(me.cn,"恭喜，添加成功！");
													
													me.ds.reload();//刷新列表
													
													me.addSuccess();//添加成功之后
													
													me.afterAddSave();//在添加保存之后处理
												} else {
													Ext.fm.msg(me.cn,"恭喜，修改成功！");
													
													me.ds.reload();//刷新列表
													
													me.afterModifySave();//在修改保存之后处理
												}
											},
											failure : function(form, action) {
												Ext.Msg.show({
													title : '错误提示',
													msg : action.result.msg,
													buttons : Ext.Msg.OK,
													icon : Ext.Msg.ERROR
												});
											}
										});
					}
				}
			});

	this.btn_cancel = new Ext.Button({// 取消按钮
		text : '取消',
		iconCls : 'remove',
		scope:this,
		handler : function() {			
			this.formWindow.hide();
		}
	});
	
	this.getForm = function(){
		var me = this;
		
		if(me.addLinkName != null  && me.addLinkName != ""){
			me.form = me[me.addLinkName];
			return;
		}
		
		if(me.formColumn == 1){/** 一列显示*/
			me.form = new Ext.FormPanel({// 用户表单
				labelAlign : 'right',
				labelWidth : 70,
				bodyStyle : 'padding:2px',
				frame : true,
				border : false,
				autoScroll : true,
				fileUpload : me.formFileUpload,
				defaultType : 'textfield',
				defaults : {
					anchor : '93%'
				},
				items : me.formArray,
				buttons : [ me.btn_save, me.btn_cancel ]
			});
			
		}else if(me.formColumn == 2){/** 两列显示*/
			me.form = new Ext.FormPanel({// 用户表单
				labelAlign : 'right',
				labelWidth : 70,
				bodyStyle : 'padding:2px',
				frame : true,
				border : false,
				autoScroll : true,
				fileUpload : me.formFileUpload,
				items : [ {
					layout : 'column',
					frame : true,
					border : false,
					items : [ {
						columnWidth : .5,
						layout : 'form',
						defaultType : 'textfield',
						defaults : {
							anchor : '93%'
						},
						items : me.leftArray
					}, {
						columnWidth : .5,
						layout : 'form',
						defaultType : 'textfield',
						defaults : {
							anchor : '93%'
						},
						items : me.rightArray
					} ]
				} ],
				buttons : [ me.btn_save, me.btn_cancel ]
			});

		}
	};
	
	this.getForm();//获取表单调用方法
	
	this.formWindow = new Ext.Window({
		title : this.cn,
		layout : 'fit',
		width : 500,
		height : 300,
		closeAction : 'hide',
		plain : true,
		items : this.form
	});

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

	this.toolbar = new Ext.FlyingToolbar({// 操作工具栏
		items : this.buttonArray
	});

	this.grid = new Ext.grid.FlyingGridPanel({// 列表
		iconCls : 'icon-grid',
		margins : '2 2 2 2',
		loadMask : {
			msg : '数据加载中...'
		},
		region : 'center',
		cm : this.cmArray,
		ds : this.ds,
		bbar : this.pagingBar,
		tbar : this.toolbar,
		sm : new Ext.grid.CheckboxSelectionModel({}),
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
		title : this.cn,
		layout : 'border',
		items : [ this.grid ]
	});
}