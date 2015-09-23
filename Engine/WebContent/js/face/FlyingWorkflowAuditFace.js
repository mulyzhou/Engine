function FlyingWorkflowAuditFace(configs) {
	/** 全局属性区 */
	this.configs = configs || {};// 配置属性
	
	this.submitUrl = "common.action?command=workflow.autoRun";//提交地址
	
	this.pk = '';// 主键名称
	
	this.parent = null;//父对象
	
	this.params = {};//请求参数
	
	this.comments = {};//意见区
	
	this.formArea = {};
	
	
	/** 初始化操作区 */
	this.Constructor = function() {// 构造方法
		var me = this;

		for ( var name in me.configs) {// 初始化属性
			me[name] = me.configs[name];
		}
		
		me.formArea = new Ext.Panel({
    		html : "<div id='loading'><div class='loading-indicator'><img src='img/loading32.gif' width='31' height='31'style='margin-right: 8px; float: left; vertical-align: top;' />数据加载中…… <br /></div></div>"+
    		"<iframe id='flowformAudit' src='GetFormServlet?show=true&INSTANCE_ID="+me.params["INSTANCE_ID"]+"' frameborder='0' width='100%' height='100%'></iframe>"
		});
	};
	
	this.Constructor();	
	
	this.init = function(){
		Ext.get('flowformAudit').dom.onload = function() {
			Ext.get('loading').remove();
    	};
	};
	
	this.initComment = function(){
		var me = this;
		
		var obj = {};
		
		var template = new Ext.XTemplate(
				'<tpl for="data">',
					'<tpl if="this.isStart(STEP_TYPEID)">',
						'<p>',
						'<span class="evidence">申请人：</span>{USER_NAME}<span class="blank"></span>',
						'在 {OPINION_TIME}发起流程 <span class="blank"></span>',
						'</p>',
					'</tpl>',
					'<tpl if="this.isJudge(STEP_TYPEID)">',
						'<p>',
						'<span class="evidence">判断条件：</span>{STEP_NAME}<span class="blank"></span>',
						'<span class="evidence">判断结果：</span>{ACTION_NAME}<span class="blank"></span>',
						'</p>',
					'</tpl>',
					'<tpl if="this.isAudit(STEP_TYPEID)">',
						'<tpl if="this.isState(STATE)">',
							'<p>',
							'<span class="evidence">审批人：</span>{USER_NAME}<span class="blank"></span>',
							'在 {OPINION_TIME}审批 <span class="blank"></span>',
							'<span class="evidence">审批决定：</span>{ACTION_NAME}<span class="blank"></span>',
							'<span class="evidence">审批意见：</span>{OPINION_DESC}',
							'</p>',
						'</tpl>',
						'<tpl if="this.isState(STATE) == false">',
							'<p>',
							'<span class="evidence">流程(待审核)状态：</span>{STEP_NAME}<span class="blank"></span>',
							'</p>',
						'</tpl>',
					'</tpl>',
					'<tpl if="this.isEnd(STEP_TYPEID)">',
						'<p>',
						'<span class="evidence">流程审核结束</span><span class="blank"></span>',
						'</p>',
					'</tpl>',
		        '</tpl></p>',
		        {
		            isStart: function(STEP_TYPEID){
		                return STEP_TYPEID == "C36CFF852FBA4F3EB6A061E8063B1C20";
		            },
		            isAudit: function(STEP_TYPEID){
		                return STEP_TYPEID == "92ABBD1AD88A45458670ADCAC8A741A6";
		            },
		            isJudge: function(STEP_TYPEID){
		            	return STEP_TYPEID == "3F9BAF91909447E982158445DC13FA94";
		            },
		            isParallel: function(STEP_TYPEID){
		            	return STEP_TYPEID == "61C2F6F6EF974E75999E32968FD1CFA9";
		            },
		            isCombine: function(STEP_TYPEID){
		            	return STEP_TYPEID == "D01926902F284847BF84BCDA430EC93B";
		            },
		            isEnd: function(STEP_TYPEID){
		            	return STEP_TYPEID == "A51E21BF95744487B6AC150CD1B1975D";
		            },
		            isState: function(STATE){
		            	return STATE == 0;
		            }
		        }
		);
			
		Ext.Ajax.request({                           
			url : 'common.action?command=T_WF_OPINION.selectByOpinionId',
			params : me.params,
			async: false,
	        method:'GET',
	        success:function(response, opts){
	        	obj = Ext.util.JSON.decode(response.responseText);
	        }
	    });
		
		me.comments = template.apply(obj);
	};

	this.initComment();
	
	/** 表单按钮区 */
	this.btn_save = new Ext.Button({// 保存按钮
		text : '提交',
		iconCls : 'save',
		scope:this,
		handler : function(btn) {
			var me = this;
			//INSTANCE_ID 无需使用，避免传入引入不可控错误
			delete me.params["INSTANCE_ID"];
			
			if (me.form.getForm().isValid()) {
				me.form.getForm().submit({
					url : me.submitUrl,
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
						};
						
						me.parent.afterSubmit();
					},
					failure : function(form, action) {
						Ext.Msg.show({
							title : '错误提示',
							msg : '网络或者服务器出现异常，无法连接!',
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.ERROR
						});
					}
				});
			}
		}
	});
	//选择多选框
	this.combo = {
		xtype : 'combo',
		allowBlank : false,
		fieldLabel : "您的选择",
		hiddenName : "ACTION_ID",
		valueField : "ACTION_ID",
		displayField : "ACTION_NAME",
		mode : 'remote',
		pageSize : 10,
		minChars : 1,
		store : new Ext.data.Store({
			url : "common.action?command=T_WF_ACTION.selectByOpinionId",
			baseParams : this.params,
			reader : new Ext.data.JsonReader({
				root : 'data',
				totalProperty : "total"
			}, [ {
				name : "ACTION_ID"
			}, {
				name : "ACTION_NAME"
			} ])
		}),
		selectOnFocus : true,
		triggerAction : 'all',
		loadingText : '加载中...'
	};
	
	// 进行审批
	this.form = new Ext.FormPanel({// 用户表单
		region : "center",
		labelAlign : 'right',
		labelWidth : 70,
		bodyStyle : 'padding:2px',
		frame : true,
		border : false,
		autoScroll : true,
		defaults : {
			anchor : '93%'
		},
		items : [{
			xtype:'fieldset',
            title: '审批信息区',
            collapsible: true,
            autoHeight: true,
            items: this.formArea
		},{
			xtype:'fieldset',
            title: '审批意见区',
            collapsible: true,
            collapsed : true,
            autoHeight: true,
            items:[{
            	xtype: 'panel',
            	html: this.comments
            }]	
        },{
        	xtype:'fieldset',
            title: '审批区',
            collapsible: true,
            autoHeight: true,
            items:[this.combo,{
    			xtype : 'textarea',
    			fieldLabel : '您的意见',
    			anchor : '93%',
    			allowBlank : false,
    			name : 'OPINION_DESC'
    		} ]
        }],
        buttons : [ this.btn_save ]
	});
	
	this.center = new Ext.Panel({
		layout : 'border',
		frame : true,
		border : false,
		buttonAlign : 'center',
		items : [this.form]
	});
}