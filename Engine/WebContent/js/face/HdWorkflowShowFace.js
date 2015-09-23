function HdWorkflowShowFace(configs) {
	/** 全局属性区 */
	this.configs = configs || {};// 配置属性
	
	this.params = {};//请求参数
	
	this.comments = {};//意见区
	
	this.formArea = {};
	
	/** 初始化操作区 */
	this.Constructor = function() {// 构造方法
		var me = this;

		for ( var name in me.configs) {// 初始化属性
			me[name] = me.configs[name];
		}
		
		var obj = Ext.fcache.getByUrl(me.params["FORM_ADDR"],"hdbusinessshow",me.params);
		
		me.formArea = new Ext.Panel({
			region : 'north',
			layout : 'fit',
			height : 200,
			items : obj.center
		});
	};
	
	this.Constructor();
	
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
						'<tpl if="this.isRecall(INSTANCESTATE) == false">',
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
						'<tpl if="this.isRecall(INSTANCESTATE)">',
							'<p>',
							'<span class="evidence">流程被撤回，请进行修改后，提交！</span><span class="blank"></span>',
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
		            },
		            isRecall: function(INSTANCESTATE){
		            	return INSTANCESTATE == 2;
		            }
		        }
		);
			
		Ext.Ajax.request({                           
			url : 'common.action?command=T_WF_OPINION.selectByInstanceId',
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
            title: '审批意见区',
            collapsible: true,
            autoHeight: true,
            items:[{
            	xtype: 'panel',
            	html: this.comments
            }]	
        }]
	});
	
	this.center = new Ext.Panel({
		layout : 'border',
		frame : true,
		border : false,
		buttonAlign : 'center',
		items : [this.formArea,this.form]
	});
}