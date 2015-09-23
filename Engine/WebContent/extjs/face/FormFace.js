function FormFace(configs,parent) {
	/** 全局属性区 */
	this.configs = configs || {};// 配置属性
	
	this.parent = parent;//父亲对象
	
	this.url = "";//添加请求url
	
	this.formColumn = 2;//表单列数
	
	this.formFileUpload = false;//表单是否含有上传控件
	
	this.formLabelWidth = 70;//表单里面控件前面的标题长度
	
	this.formArray = new Array();// 表单数组
	
	/** 初始化操作区 */
	this.Constructor = function() {// 构造方法

		for ( var name in this.configs) {// 初始化属性
			if(!(this.parent != null && typeof this.configs[name] == "function")){
				this[name] = this.configs[name];
			}
		}
		
		this.formArray = Ext.futil.handleForm(this.columns,this.formColumn);

	};
	
	this.Constructor();
	
	this.kindeditorSync = function() {

		for ( var i = 0; i < this.columns.length; i++) {// 遍历
			if (this.columns[i]["isPk"]) {// 对主键进行处理
				continue;
			}
			
			if (this.columns[i]["isForm"] == false) {// 不做为表单项
				continue;
			}
			
			if(this.columns[i]["xtype"] == "kindeditor"){
				this.form.getForm().findField(this.columns[i]["dataIndex"]).editor.sync();
			}
		}
	};
	
	this.clickSave = function(b,e){
		if(this.parent){
			this.parent.clickSave(b,e);
		}
	};
	
	this.clickCancel = function(b,e){
		if(this.parent){
			this.parent.clickCancel(b,e);
		}
	};
	
	/** 表单按钮区 */
	this.btn_save = new Ext.Button({// 保存按钮
		text : '保存',
		iconCls : 'save',
		scope:this,
		handler : this.clickSave
	});

	this.btn_cancel = new Ext.Button({// 取消按钮
		text : '取消',
		iconCls : 'remove',
		scope:this,
		handler : this.clickCancel
	});
	
	this.getForm = function(){
		
		if(this.formColumn == 1){/** 一列显示*/
			this.form = new Ext.FormPanel({// 用户表单
				labelAlign : 'right',
				labelWidth : this.formLabelWidth,
				bodyStyle : 'padding:2px',
				frame : true,
				border : false,
				autoScroll : true,
				fileUpload : this.formFileUpload,
				defaultType : 'textfield',
				defaults : {
					anchor : '93%'
				},
				items : this.formArray[0],
				buttons : [ this.btn_save, this.btn_cancel ]
			});
			
		}else if(this.formColumn == 2){/** 两列显示*/
			this.form = new Ext.FormPanel({// 用户表单
				labelAlign : 'right',
				labelWidth : this.formLabelWidth,
				bodyStyle : 'padding:2px',
				frame : true,
				border : false,
				autoScroll : true,
				fileUpload : this.formFileUpload,
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
						items : this.formArray[0]
					}, {
						columnWidth : .5,
						layout : 'form',
						defaultType : 'textfield',
						defaults : {
							anchor : '93%'
						},
						items : this.formArray[1]
					} ]
				} ],
				buttons : [ this.btn_save, this.btn_cancel ]
			});

		}
	};
	
	this.getForm();//获取表单调用方法
	
	/** 容器 */
	this.center = new Ext.Panel({
		layout : 'fit',
		border : false,
		items : [ this.form ]
	});
}