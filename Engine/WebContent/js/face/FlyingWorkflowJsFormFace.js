function FlyingWorkflowJsFormFace(configs) {
	/** 全局属性区 */
	this.configs = configs || {};// 配置属性
	
	this.pk = '';// 主键名称
	
	this.parent = null;//父对象
	
	this.params = {};//请求参数
	
	this.formColumn = 2;//表单列数
	
	this.formFileUpload = false;//表单是否含有上传控件
	
	this.formLabelWidth = 70;//表单里面控件前面的标题长度
	
	this.leftArray = new Array();// 表单左侧数组

	this.rightArray = new Array();// 表单右侧数组

	this.formArray = new Array();// 表单数组
	
	this.form = {};//表单项
	
	this.isUpload = true;//是否需要上传
	
	this.beforeAddSave = Ext.emptyFn;//添加保存之前方法
	
	this.afterAddSave = Ext.emptyFn;//添加保存之后方法
	
	/** 初始化操作区 */
	this.Constructor = function() {// 构造方法
		var me = this;

		for ( var name in me.configs) {// 初始化属性
			me[name] = me.configs[name];
		}
		
		me.handleForm();// 处理表单
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
		if (formObj["maxValue"] != undefined) {// 最大值
			packFormObj["maxValue"] = formObj["maxValue"];
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
		
		if (formObj["listeners"] != undefined) {// 添加事件
			var lis = formObj["listeners"];
			for(var name in lis){
				if(typeof(lis[name]) == "function"){
					lis[name] = lis[name].createDelegate(me);
				}
			}
			
			packFormObj["listeners"] = lis;
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
		} else if(formObj["xtype"] == "numberfield"){
			// 对于类型的处理的预留区
			if (formObj["decimalPrecision"] != undefined) {// 提示显示的位置
				packFormObj["decimalPrecision"] = formObj["decimalPrecision"];
			}
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
	
	this.Constructor();
	
	this.getForm = function(){
		var me = this;
		
		if(me.isUpload){
			if(me.formColumn == 1){
				me.formArray.push(new Ext.flying.upload());
			}else if(me.formColumn == 2){
				me.leftArray.push(new Ext.flying.upload());
			}
		}
		
		if(me.formColumn == 1){/** 一列显示*/
			me.form = new Ext.FormPanel({// 用户表单
				labelAlign : 'right',
				labelWidth : me.formLabelWidth,
				bodyStyle : 'padding:2px',
				frame : true,
				border : false,
				autoScroll : true,
				fileUpload : me.formFileUpload,
				defaultType : 'textfield',
				defaults : {
					anchor : '93%'
				},
				items : me.formArray
			});
			
		}else if(me.formColumn == 2){/** 两列显示*/
			me.form = new Ext.FormPanel({// 用户表单
				labelAlign : 'right',
				labelWidth : me.formLabelWidth,
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
				} ]
			});

		}
	};
	
	this.getForm();//获取表单调用方法
	
	/** 表单按钮区 */
	this.btn_save = new Ext.Button(
	{// 保存按钮
		text : '保存',
		iconCls : 'save',
		scope:this,
		handler : function(btn) {
			var me = this;
			
			var url = "common.action?command="+me.en + ".insert";
			
			if (me.beforeAddSave() == false){
				return;
			}
			
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
						
						Ext.fm.msg(me.cn,"恭喜，添加成功！");
						
						me.afterAddSave();//在添加保存之后处理
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
	
	this.center = new Ext.Panel({
		frame : true,
		border : false,
		autoScroll : true,
		buttonAlign : 'center',
		items : [this.form],
		buttons : [ this.btn_save ]
	});
}