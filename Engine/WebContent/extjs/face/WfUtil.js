Ext.apply(Ext, {
	wfutil : {
		/**触发流程*/
		triggerFlow : function(me){
			if(me.grid.getSelectionModel().hasSelection()){
				var rows = me.grid.getSelectionModel().getSelections();
				var mark = true;
				
				for(var i=0;i<rows.length;i++){
					if(rows[i].data["FLOW_STATE"] > 0){
						mark = false;
						break;
					}
				}
				
				if(!mark){
					Ext.Msg.show({
						title : '警告提示',
						msg : '业务数据已经在待审核中，请检查你选择的业务数据！',
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.WARNING
					});
					return;
				}
				
				Ext.Ajax.request({                           
			        url: 'common.action?command=T_WF_FLOWFORM.getFlowByBusinessTable',
			        method:'post',
			        params:{"BUSINESSTABLE" :me.en+'.js'},
			        success:function(response, opts){			        	
			        	var resultObj = Ext.util.JSON.decode(response.responseText);
			        	
			        	if(resultObj.data.length == 0){
			        		Ext.Msg.show({
								title : '警告提示',
								msg : '业务没有关联的流程！',
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.WARNING
							});
			        	}else if(resultObj.data.length > 1){
			        		var str = '';
			        		for(var m=0;m<resultObj.data.length;m++){
			        			str += resultObj.data[m]["FLOW_NAME"] + ",";
			        		}
			        		
			        		str =  str.substr(0,str.length -1);
			        		
			        		Ext.Msg.show({
								title : '警告提示',
								msg : '业务管理多个流程，分别是：' + str,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.WARNING
							});
			        	}else{
			        		var flowId = resultObj.data[0]["FLOW_ID"];
			        		
			        		Ext.Ajax.request({
								url : "common.action?command=T_WF_OPINION.selectStartStepAuditPerson",
								params : {"FLOW_ID" : flowId},
								success : function(response, opts) {	
									var auditResultObj = Ext.util.JSON.decode(response.responseText);
									
									me.auditUserId = "";
									if(auditResultObj.data.length == 0){
										Ext.Msg.show({
											title : '异常提示',
											msg : "下一步无审核人员，请联系管理员，分配审核人员!",
											buttons : Ext.Msg.OK,
											icon : Ext.Msg.WARNING
										});
										return ;
									}else if(auditResultObj.data.length == 1){
										me.auditUserId = auditResultObj.data[0]["USER_ID"];
									}else if(auditResultObj.data.length > 1){
										
										var auditChecks = new Array();

										for(var i =0;i<auditResultObj.data.length;i++){
											var auditObj = {};
											auditObj["boxLabel"] = auditResultObj.data[i]["USER_NAME"];
											auditObj["name"] = "AUDIT_USER_ID";
											auditObj["inputValue"] = auditResultObj.data[i]["USER_ID"];
											auditObj["listeners"] = {
													"scope" : me,
													"check" : function(radio,checked){
														if(checked){
															Ext.Msg.confirm('下一步审核人', '确定让'+radio.boxLabel+'审核吗?', function(btn) {
																if (btn == 'yes') {
																	me.auditUserId = radio.inputValue;																	
																	
																	var businessIds = "";
																	
																	for(var i=0;i<rows.length;i++){
																		businessIds += rows[i].data[me.pk] + ",";
																	}
																	
																	businessIds =  businessIds.substr(0,businessIds.length -1);
																	Ext.Ajax.request({
																		url: 'common.action?command=workflow.triggerFlow',
																        method:'post',
																        params:{"businessIds" : businessIds,"businessTable" :me.en,"FLOW_ID" : flowId,"AUDIT_USER_ID" : me.auditUserId},
																		success : function(response, opts) {	
																			me.auditUserWin.close();
																			Ext.fm.msg("生产计划","恭喜，保存成功！");
																			me.ds.reload();
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
															})
														}
													}
											};
											
											auditChecks.push(auditObj);
										}

										me.auditUserWin = new Ext.Window({
											title : "选择审核人员",
											layout : 'fit',
											width : 500,
											height : 200,
											frame : true,
											items : {
												xytpe: 'panel',
												frame : true,
												items : {
													 xtype: 'radiogroup',
													 style : 'padding:10px 50px 10px 50px;',
										             fieldLabel: '审核人员',
										             items: auditChecks
												}
											}
										});
										
										me.auditUserWin.show();
									}
									
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
			        },
			        failure: function(){
			        	Ext.Msg.show({
							title : '错误提示',
							msg : '初始化失败',
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.ERROR
						});
			        }
				});
			}
		},
		/**查看生产计划*/
		hdShow : function(me){
			if(me.grid.getSelectionModel().hasSelection()){
				var rows = me.grid.getSelectionModel().getSelections();
				var type = rows[0].data[me.pk];
				var instanceIds = "";
				var mark = true;
				
				for(var i=0;i<rows.length;i++){
					
						instanceIds += rows[i].data[me.pk] + ",";
					
				}
				
				instanceIds =  instanceIds.substr(0,instanceIds.length -1);
				
				if(mark){
					Ext.Ajax.request({                           
				        url: 'common.action?command=workflow.hdCollectionShow',
				        method:'post',
				        params:{"instanceIds" : instanceIds,"en" :me.en},
				        success:function(response, opts){
				        	var resultObj = Ext.util.JSON.decode(response.responseText);

				        	me.showAuditWin = new Ext.Window({
			            		title : me.cn,
			            		layout : 'fit',
			            		width : 850,
			            		height : 600,
			            		plain : true,
			            		maximizable: true,
			            		html : resultObj.data
			            	});
				        	
				        	me.showAuditWin.show();
				        	me.showAuditWin.maximize();
				        },
				        failure: function(){
				        	Ext.Msg.show({
								title : '错误提示',
								msg : '初始化失败',
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.ERROR
							});
				        }
				        
					})
				}else{
					Ext.Msg.show({
						title : '友情提示',
						msg : '您选择了不同类型的生产计划，请选择相同类型的生产计划进行查看！',
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.WARNING
					});
				}
			}else{
				Ext.Msg.show({
					title : '友情提示',
					msg : '请选择一条记录！',
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.WARNING
				});
			}
		},
		/**导出生产计划*/
		hdDownload : function(me){
			if(me.grid.getSelectionModel().hasSelection()){
				var rows = me.grid.getSelectionModel().getSelections();
				var type = rows[0].data[me.pk];
				var instanceIds = "";
				var mark = true;
				
				for(var i=0;i<rows.length;i++){
					
						instanceIds += rows[i].data[me.pk] + ",";
					
				}
				instanceIds =  instanceIds.substr(0,instanceIds.length -1);
				if(mark){
					var exportForm = new Ext.FormPanel();
			    	exportForm.applyToMarkup(Ext.DomHelper.append(Ext.getBody(), {
						tag : "div"
					}, true));
			    	exportForm.getForm().getEl().dom.action = "BbDownload?instanceIds=" + instanceIds + "&en=" + me.en;  
			    	exportForm.getForm().getEl().dom.submit();
				}else{
					Ext.Msg.show({
						title : '友情提示',
						msg : '您选择了不同类型的生产计划，请选择相同类型的生产计划进行查看！',
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.WARNING
					});
				}
			}else{
				Ext.Msg.show({
					title : '友情提示',
					msg : '请选择一条记录！',
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.WARNING
				});
			}
		},
		/**获取流程状态*/
		"flowState" : function(row,pk){
			var red = "#FF6464", yellow = "#FFFF64", gray = "#BFBFBF", blue = "blue", state;
			if (row.data["FLOW_STATE"] == '1') {
				state = "<span style='background-color:"
					+ red
					+ ";width: 16px;height: 16px;vertical-align: middle;display: inline-block'></span><span  style='color:"
					+ red + "'>流程待审</span><a  href='javascript:void(0)' onclick='Ext.wfutil.showFlowStateDetails(\""
				+ row.data[pk] + "\")'><span class='open'></span></a>";
			} else if (row.data["FLOW_STATE"] == '2') {
				state = "<span style='background-color:"
					+ gray
					+ ";width: 16px;height: 16px;vertical-align: middle;display: inline-block'></span><span  style='color:"
					+ gray + "'>流程完成</span><a  href='javascript:void(0)' onclick='Ext.wfutil.showFlowStateDetails(\""
				+ row.data[pk] + "\")'><span class='open'></span></a>";
			}

			return state;
		},
		/**查看流程详情*/
		"showFlowStateDetails" : function(bizId) {
			var me = this;
			
			Ext.Ajax.request({
				url : 'common.action?command=T_WF_OPINION.selectFlowStateDetails',
				params : {
					BUSINESS_ID : bizId
				},
				method : 'post',
				success : function(response, opts) {
					var resultObj = Ext.util.JSON.decode(response.responseText);
					
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
					
					var flowStatePanel = new Ext.Panel({
						frame : true,
						style : 'padding:10px 20px 10px 20px;',
						html : template.apply(resultObj)
					});
					
					if(me.flowStateWin){
						me.flowStateWin.close();
					}
					
					me.flowStateWin = new Ext.Window({
		    			title : '流程详情',
		    			width : 830,
		    			height : 450,
		    			frame : true,
		    			autoScoll : true,
		    			closeAction : 'hide',
		    			items : flowStatePanel
		    		});
					
					me.flowStateWin.show();
				},
				failure : function() {
					Ext.Msg.show({
						title : '错误提示',
						msg : '操作失败',
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.ERROR
					});
				}
			});
		}
	}	
})