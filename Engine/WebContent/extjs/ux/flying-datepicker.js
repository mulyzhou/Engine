/**
 * 集成datepicker
 * @author zdf 2012-10-17
 */
//Ext.form.DatepickerPanel = Ext.extend(Ext.form.TextField, {
//		dpConfig : {},
//	    onRender : function(ct, position){   
//			Ext.form.DatepickerPanel.superclass.onRender.call(this, ct, position);
//
//			this.el.addListener("click",this.addDatePicker,this);
//		},
//		addDatePicker : function(){
//			WdatePicker(this.dpConfig);
//		}
//});

Ext.form.DatepickerPanel = Ext.extend(Ext.form.TriggerField, {
	/**
	 * @param {Array} date97配置列表
	 * @type 
	 */
	dateConfig:null,
	/**
	 * @param {Boolean} 是否显示时间,默认为不显示
	 */
	time:false,
	triggerClass : 'x-form-date-trigger',
	defaultAutoCreate : {tag : "input",type : "text",size : "10",autocomplete : "off"},
	initComponent : function() {
		Ext.form.DateField.superclass.initComponent.call(this);
		this.initDateConfig();
	},
	onTriggerClick : function(e) {// 点击查找按钮时
		if (this.disabled) {
			return;
		}
		this.onFocus({});
		var bodyWidth = document.body.clientWidth;
		var xC = document.body.clientWidth - e.xy[0] - this.width;
		var yC = document.body.clientHeight - e.xy[1] - this.height; 
		var x=0;
		var y=0;
		if (xC > 0)
			x = e.xy[0];
		else
			x = document.body.clientWidth - this.width - 4;

		if (yC > 0)
			y = e.xy[1];
		else
			y= document.body.clientHeight - this.height - 4;
//		this.dateConfig['position']={left:e.xy[0],top:e.xy[1]};
		WdatePicker(this.dateConfig);
	},
	initDateConfig:function()
	{
		if(!this.dateConfig)
			this.dateConfig=new Array();
		if(!this.dateConfig['el'])
			this.dateConfig['el']=this.id;
		if(this.time)
			this.addDateConfig("dateFmt",'yyyy-MM-dd HH:mm:ss');
		else
			if(!this.dateConfig["dateFmt"])
				this.dateConfig["dateFmt"]='yyyy-MM-dd';
		if(!this.dateConfig["skin"])
			this.dateConfig["skin"]='ext';
	},
	addDateConfig:function(name,value)
	{
		this.removeDateConfig(name);
		this.dateConfig[name]=value;
	},
	removeDateConfig:function(name){
		for (var i = 0; i < this.dateConfig.length; i++) {
			var temp = this.dateConfig[i];
			if (temp && temp.split(':')[0] == name) {
				this.dateConfig.pop(i);
				return;
			}
		}
	},
	setDateConfig:function(config)
	{
		this.dateConfig=config;
		this.initDateConfig();
	}
});

Ext.reg('datepicker',Ext.form.DatepickerPanel);  