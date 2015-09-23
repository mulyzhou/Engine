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
'�ֶα�'
/

comment on column T_BASE_FIELD.ZDID is
'�ֶ�ID'
/

comment on column T_BASE_FIELD.ZDMC is
'�ֶ�����'
/

comment on column T_BASE_FIELD.ZDLX is
'�ֶ�����'
/

comment on column T_BASE_FIELD.ZDCD is
'�ֶγ���'
/

comment on column T_BASE_FIELD.SFZJ is
'�Ƿ�����'
/

comment on column T_BASE_FIELD.SFWK is
'�Ƿ��Ϊ��'
/

comment on column T_BASE_FIELD.MRZ is
'Ĭ��ֵ'
/

comment on column T_BASE_FIELD.ZDZS is
'�ֶ�ע��'
/

comment on column T_BASE_FIELD.BID is
'��ID'
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
'�����'
/

comment on column T_BASE_FOREIGNKEY.WJID is
'���ID'
/

comment on column T_BASE_FOREIGNKEY.WJMC is
'�������'
/

comment on column T_BASE_FOREIGNKEY.ZBMC is
'��������'
/

comment on column T_BASE_FOREIGNKEY.WBMC is
'�������'
/

comment on column T_BASE_FOREIGNKEY.ZBZD is
'�����ֶ�'
/

comment on column T_BASE_FOREIGNKEY.WBZD is
'����ֶ�'
/

comment on column T_BASE_FOREIGNKEY.STATE is
'����״̬'
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
'�˵�'
/

comment on column T_BASE_MENU.ID is
'�˵�ID'
/

comment on column T_BASE_MENU.CJ is
'�˵��㼶'
/

comment on column T_BASE_MENU.PID is
'��ID'
/

comment on column T_BASE_MENU.TEXT is
'�˵�����'
/

comment on column T_BASE_MENU.TYPE is
'ģ������'
/

comment on column T_BASE_MENU.ICON is
'ͼƬ��ַ'
/

comment on column T_BASE_MENU.SRC is
'�����ַ'
/

comment on column T_BASE_MENU.EXPANDED is
'�Ƿ�չ��'
/

comment on column T_BASE_MENU.CACHE is
'�Ƿ����û���'
/

comment on column T_BASE_MENU.LEAF is
'�Ƿ���Ҷ�ӽڵ�'
/

comment on column T_BASE_MENU.DESKTOP is
'�Ƿ����������'
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
'����'
/

comment on column T_BASE_STATICS.BBID is
'����ID'
/

comment on column T_BASE_STATICS.BBMC is
'��������'
/

comment on column T_BASE_STATICS.SQLID is
'ִ��sqlid'
/

comment on column T_BASE_STATICS.BBJY is
'�������'
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
'���ݻ�����'
/

comment on column T_BASE_TABLE.BID is
'��ID'
/

comment on column T_BASE_TABLE.BMC is
'������'
/

comment on column T_BASE_TABLE.BZS is
'��ע��'
/

comment on column T_BASE_TABLE.BMS is
'������'
/

comment on column T_BASE_TABLE.X is
'������'
/

comment on column T_BASE_TABLE.Y is
'������'
/

comment on column T_BASE_TABLE.HEIGHT is
'�߶�'
/

comment on column T_BASE_TABLE.WIDTH is
'���'
/

comment on column T_BASE_TABLE.STATE is
'״̬'
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

