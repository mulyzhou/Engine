create sequence SEQ_BASE_FIELD
increment by 1
start with 1
 nomaxvalue
 nominvalue
 cache 10
/

create sequence SEQ_BASE_FOREIGNKEY
start with 1
increment by 1
 nomaxvalue
 nominvalue
 cache 10
/

create sequence SEQ_BASE_MENU
increment by 1
start with 1
 nomaxvalue
 nominvalue
 cache 10
/

create sequence SEQ_BASE_STATICS
increment by 1
start with 1
 nomaxvalue
 nominvalue
 cache 10
/

create sequence SEQ_BASE_TABLE
increment by 1
start with 1
 nomaxvalue
 nominvalue
 cache 10
/

/*==============================================================*/
/* Table: T_BASE_FIELD                                          */
/*==============================================================*/
create table T_BASE_FIELD  (
   ZDID                 NUMBER(11)                      not null,
   ZDMC                 VARCHAR2(50 BYTE),
   ZDLX                 VARCHAR2(50 BYTE),
   ZDCD                 VARCHAR2(50 BYTE),
   SFZJ                 NUMBER(1),
   SFWK                 NUMBER(1),
   MRZ                  VARCHAR2(200 BYTE),
   ZDZS                 VARCHAR2(50 BYTE),
   BID                  NUMBER(11),
   constraint PK_T_BASE_FIELD primary key (ZDID)
)
/

comment on table T_BASE_FIELD is
'字段表'
/

comment on column T_BASE_FIELD.ZDID is
'字段ID'
/

comment on column T_BASE_FIELD.ZDMC is
'字段名称'
/

comment on column T_BASE_FIELD.ZDLX is
'字段类型'
/

comment on column T_BASE_FIELD.ZDCD is
'字段长度'
/

comment on column T_BASE_FIELD.SFZJ is
'是否主键'
/

comment on column T_BASE_FIELD.SFWK is
'是否可为空'
/

comment on column T_BASE_FIELD.MRZ is
'默认值'
/

comment on column T_BASE_FIELD.ZDZS is
'字段注释'
/

comment on column T_BASE_FIELD.BID is
'表ID'
/

/*==============================================================*/
/* Table: T_BASE_FOREIGNKEY                                     */
/*==============================================================*/
create table T_BASE_FOREIGNKEY  (
   WJID                 NUMBER(11)                      not null,
   WJMC                 VARCHAR2(100 BYTE),
   ZBMC                 VARCHAR2(100 BYTE),
   WBMC                 VARCHAR2(100 BYTE),
   ZBZD                 VARCHAR2(100 BYTE),
   WBZD                 VARCHAR2(100 BYTE),
   STATE                NUMBER(11),
   constraint PK_T_BASE_FOREIGNKEY primary key (WJID)
)
/

comment on table T_BASE_FOREIGNKEY is
'外键表'
/

comment on column T_BASE_FOREIGNKEY.WJID is
'外键ID'
/

comment on column T_BASE_FOREIGNKEY.WJMC is
'外键名称'
/

comment on column T_BASE_FOREIGNKEY.ZBMC is
'主表名称'
/

comment on column T_BASE_FOREIGNKEY.WBMC is
'外表名称'
/

comment on column T_BASE_FOREIGNKEY.ZBZD is
'主表字段'
/

comment on column T_BASE_FOREIGNKEY.WBZD is
'外表字段'
/

comment on column T_BASE_FOREIGNKEY.STATE is
'生成状态'
/

/*==============================================================*/
/* Table: T_BASE_MENU                                           */
/*==============================================================*/
create table T_BASE_MENU  (
   ID                   NUMBER(11)                      not null,
   CJ              NUMBER(11),
   PID                  NUMBER(11),
   TEXT                 VARCHAR2(50 BYTE),
   TYPE                 VARCHAR2(50 BYTE),
   ICON                 VARCHAR2(200 BYTE),
   SRC                  VARCHAR2(200 BYTE),
   EXPANDED             NUMBER(1),
   CACHE                NUMBER(1),
   LEAF                 NUMBER(1),
   DESKTOP              NUMBER(1),
   constraint PK_T_BASE_MENU primary key (ID)
)
/

comment on table T_BASE_MENU is
'菜单'
/

comment on column T_BASE_MENU.ID is
'菜单ID'
/

comment on column T_BASE_MENU.CJ is
'菜单层级'
/

comment on column T_BASE_MENU.PID is
'父ID'
/

comment on column T_BASE_MENU.TEXT is
'菜单名称'
/

comment on column T_BASE_MENU.TYPE is
'模板类型'
/

comment on column T_BASE_MENU.ICON is
'图片地址'
/

comment on column T_BASE_MENU.SRC is
'请求地址'
/

comment on column T_BASE_MENU.EXPANDED is
'是否展开'
/

comment on column T_BASE_MENU.CACHE is
'是否启用缓存'
/

comment on column T_BASE_MENU.LEAF is
'是否是叶子节点'
/

comment on column T_BASE_MENU.DESKTOP is
'是否放置在桌面'
/

/*==============================================================*/
/* Table: T_BASE_STATICS                                        */
/*==============================================================*/
create table T_BASE_STATICS  (
   BBID                 NUMBER(11)                      not null,
   BBMC                 VARCHAR2(100 BYTE),
   SQLID                VARCHAR2(100 BYTE),
   BBJY                 VARCHAR2(1000 BYTE),
   constraint PK_T_BASE_STATICS primary key (BBID)
)
/

comment on table T_BASE_STATICS is
'报表'
/

comment on column T_BASE_STATICS.BBID is
'报表ID'
/

comment on column T_BASE_STATICS.BBMC is
'报表名称'
/

comment on column T_BASE_STATICS.SQLID is
'执行sqlid'
/

comment on column T_BASE_STATICS.BBJY is
'报表语句'
/

/*==============================================================*/
/* Table: T_BASE_TABLE                                          */
/*==============================================================*/
create table T_BASE_TABLE  (
   BID                  NUMBER(11)                      not null,
   BMC                  VARCHAR2(50 BYTE),
   BZS                  VARCHAR2(50 BYTE),
   BMS                  VARCHAR2(200 BYTE),
   X                    NUMBER(11),
   Y                    NUMBER(11),
   HEIGHT               NUMBER(11),
   WIDTH                NUMBER(11),
   STATE                NUMBER(11),
   constraint PK_T_BASE_TABLE primary key (BID)
)
/

comment on table T_BASE_TABLE is
'数据基础表'
/

comment on column T_BASE_TABLE.BID is
'表ID'
/

comment on column T_BASE_TABLE.BMC is
'表名称'
/

comment on column T_BASE_TABLE.BZS is
'表注释'
/

comment on column T_BASE_TABLE.BMS is
'表描述'
/

comment on column T_BASE_TABLE.X is
'横坐标'
/

comment on column T_BASE_TABLE.Y is
'纵坐标'
/

comment on column T_BASE_TABLE.HEIGHT is
'高度'
/

comment on column T_BASE_TABLE.WIDTH is
'宽度'
/

comment on column T_BASE_TABLE.STATE is
'状态'
/

alter table T_BASE_FIELD
   add constraint FK_T_BASE_F_FK_T_BASE_T_BASE_T foreign key (BID)
      references T_BASE_TABLE (BID)
/


create trigger "TRG_BASE_FIELD" before insert
on T_BASE_FIELD for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "ZDID" uses sequence SEQ_BASE_FIELD
    select SEQ_BASE_FIELD.NEXTVAL INTO :new.ZDID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "TRG_BASE_FOREIGNKEY" before insert
on T_BASE_FOREIGNKEY for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "WJID" uses sequence SEQ_BASE_FOREIGNKEY
    select SEQ_BASE_FOREIGNKEY.NEXTVAL INTO :new.WJID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "TRG_BASE_MENU" before insert
on T_BASE_MENU for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "ID" uses sequence SEQ_BASE_MENU
    select SEQ_BASE_MENU.NEXTVAL INTO :new.ID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "TRG_BASE_STATICS" before insert
on T_BASE_STATICS for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "BBID" uses sequence SEQ_BASE_STATICS
    select SEQ_BASE_STATICS.NEXTVAL INTO :new.BBID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "TRG_BASE_TABLE" before insert
on T_BASE_TABLE for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "BID" uses sequence SEQ_BASE_TABLE
    select SEQ_BASE_TABLE.NEXTVAL INTO :new.BID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/

