"use strict";(self.webpackChunkit_blog=self.webpackChunkit_blog||[]).push([[116],{4116:(E,l,t)=>{t.r(l),t.d(l,{BloggerModule:()=>A});var e=t(6814),d=t(5861),p=t(4548),g=t(6103),s=t(5273),c=t(7639),n=t(6689),m=t(5593),r=t(9304),C=t(122),a=t(8008),P=t(3423);let M=(()=>{var u;class h{constructor(o,i){this.pdfService=o,this.noticeFunction=i}ngOnInit(){this.getCVInfo()}getCurrentYear(){return(new Date).getFullYear()}buildCVSearch(){const o=new c.G;return o.type=g.A.CV,o.pageNumber=0,o.numsPerPage=1,o}getCVInfo(){var o=this;return(0,d.Z)(function*(){const i=yield o.pdfService.filter(o.buildCVSearch());i&&0!=i.length?o.cvInfo=i[0]:o.noticeFunction.showNotice(new s.J("","Kh\xf4ng th\u1ec3 l\u1ea5y \u0111\u01b0\u1ee3c th\xf4ng tin CV",p.Q.ERROR))})()}}return(u=h).\u0275fac=function(o){return new(o||u)(n.Y36(m.F),n.Y36(r.l))},u.\u0275cmp=n.Xpm({type:u,selectors:[["app-blogger"]],decls:53,vars:3,consts:[[1,"wrapper"],[1,"content"],[1,"left"],[1,"line","title"],[1,"line"],[1,"info"],[1,"right"],[1,"title"],[3,"cvInfo"]],template:function(o,i){1&o&&(n._UZ(0,"header"),n.TgZ(1,"div",0),n._UZ(2,"breadscrumb"),n.TgZ(3,"div",1)(4,"div",2)(5,"div")(6,"div",3),n._uU(7,"CH\xc0O!"),n.qZA(),n.TgZ(8,"div",4),n._uU(9,"T\xf4i t\xean l\xe0 "),n.TgZ(10,"strong"),n._uU(11,"Tr\u1ea7n Tu\u1ea5n Anh"),n.qZA()(),n.TgZ(12,"div",4),n._uU(13,"Trang blog n\xe0y m\xecnh vi\u1ebft ra nh\u1eefng kinh nghi\u1ec7m, \u0111\u1eafng cay ng\u1ecdt b\xf9i trong qu\xe1 tr\xecnh l\xe0m vi\u1ec7c \u0111\u1ec3 chia s\u1ebb cho nh\u1eefng b\u1ea1n quan t\xe2m \u0111\u1ebfn ngh\u1ec1 l\xe0m developer."),n.qZA(),n.TgZ(14,"div",4),n._uU(15,"\u0110\xf4i n\xe9t v\u1ec1 b\u1ea3n th\xe2n :"),n.qZA(),n.TgZ(16,"ul")(17,"li"),n._uU(18,"T\u1ed1t nghi\u1ec7p kh\xf3a K14 khoa "),n.TgZ(19,"strong"),n._uU(20,"khoa h\u1ecdc m\xe1y t\xednh"),n.qZA(),n._uU(21),n.qZA(),n.TgZ(22,"li"),n._uU(23,"Th\xedch Coding, \u0111\u1ecdc c\xe1i th\xf4ng tin c\xf4ng ngh\u1ec7."),n.qZA(),n.TgZ(24,"li"),n._uU(25,"T\u01b0 t\u01b0\u1edfng: "),n.TgZ(26,"strong"),n._uU(27,"Making complex things simple"),n.qZA(),n._uU(28,"."),n.qZA()(),n.TgZ(29,"div",4),n._uU(30,"Ti\xeau ch\xed Blog:"),n.qZA(),n.TgZ(31,"ul")(32,"li"),n._uU(33,"K\u1ef9 n\u0103ng ngh\u1ec1 nghi\u1ec7p."),n.qZA(),n.TgZ(34,"li"),n._uU(35,"C\xf4ng ngh\u1ec7"),n.qZA(),n.TgZ(36,"li"),n._uU(37,"Kh\xf4ng ki\u1ebfm ti\u1ec1n t\u1eeb blog (ng\u01b0\u1ee3c l\u1ea1i c\xf2n t\u1ed1n ti\u1ec1n t\xfai duy tr\xec domain, theme, plugin \u2026)"),n.qZA()(),n.TgZ(38,"div",5)(39,"div",4)(40,"strong"),n._uU(41,"Email:"),n.qZA(),n._uU(42," tuananhbk1996@gmail.com"),n.qZA(),n.TgZ(43,"div",4)(44,"strong"),n._uU(45,"Skype:"),n.qZA(),n._uU(46," live:tuananhbk1996"),n.qZA(),n.TgZ(47,"div"),n._uU(48,"C\xe1c b\u1ea1n n\u1ebfu c\xf3 th\u1eafc m\u1eafc g\xec c\u1ee9 mail or chat chit cho m\xecnh nh\xe9."),n.qZA()()()(),n.TgZ(49,"div",6)(50,"div",7),n._uU(51,"CV C\u1ee6A T\xd4I"),n.qZA(),n._UZ(52,"pdf-render",8),n.qZA()()()),2&o&&(n.xp6(21),n.AsE(" c\u1ee7a \u0110\u1ea1i h\u1ecdc B\xe1ch Khoa - \u0110\u1ea1i h\u1ecdc Qu\u1ed1c gia Th\xe0nh ph\u1ed1 H\u1ed3 Ch\xed Minh. T\xednh \u0111\u1ebfn th\u1eddi \u0111i\u1ec3m \u0111ang vi\u1ebft nh\u1eefng d\xf2ng n\xe0y (",i.getCurrentYear(),") l\xe0 \u0111\u01b0\u1ee3c ",i.getCurrentYear()-2019," n\u0103m trong ngh\u1ec1 l\xe0m Dev."),n.xp6(31),n.Q6J("cvInfo",i.cvInfo))},dependencies:[C.G,a.e,P.a],styles:[".wrapper[_ngcontent-%COMP%]{padding:15px 60px}.wrapper[_ngcontent-%COMP%]   .content[_ngcontent-%COMP%]{display:grid;grid-template-columns:1fr 1fr}.wrapper[_ngcontent-%COMP%]   .content[_ngcontent-%COMP%]   .left[_ngcontent-%COMP%]{margin:20px 0 0}.wrapper[_ngcontent-%COMP%]   .content[_ngcontent-%COMP%]   .left[_ngcontent-%COMP%]   .line[_ngcontent-%COMP%]{margin:8px 0}.wrapper[_ngcontent-%COMP%]   .content[_ngcontent-%COMP%]   .left[_ngcontent-%COMP%]   .paragraph[_ngcontent-%COMP%]{margin:20px 0 0}.wrapper[_ngcontent-%COMP%]   .content[_ngcontent-%COMP%]   .left[_ngcontent-%COMP%]   ul[_ngcontent-%COMP%]{margin-top:0!important;padding-top:0!important;margin-bottom:0!important;padding-bottom:0!important}.wrapper[_ngcontent-%COMP%]   .content[_ngcontent-%COMP%]   .left[_ngcontent-%COMP%]   .info[_ngcontent-%COMP%]{margin-top:50px}.wrapper[_ngcontent-%COMP%]   .content[_ngcontent-%COMP%]   .left[_ngcontent-%COMP%]   .title[_ngcontent-%COMP%]{font-size:48px;font-style:normal;font-weight:700;line-height:normal;margin:0 0 20px}.wrapper[_ngcontent-%COMP%]   .content[_ngcontent-%COMP%]   .right[_ngcontent-%COMP%]   .title[_ngcontent-%COMP%]{color:var(--main-color);font-size:24px;font-style:normal;font-weight:700;line-height:normal;margin:0 0 20px;-webkit-user-select:none;user-select:none}"]}),h})();var O=t(6014),b=t(8567),_=t(8802),v=t(4551);const x=[{path:"",component:M}];let A=(()=>{var u;class h{}return(u=h).\u0275fac=function(o){return new(o||u)},u.\u0275mod=n.oAB({type:u}),u.\u0275inj=n.cJS({imports:[e.ez,b.O,_.x,v.$,O.Bz.forChild(x)]}),h})()},8008:(E,l,t)=>{t.d(l,{e:()=>s});var e=t(6689),d=t(6014),p=t(6814);function g(c,n){if(1&c&&(e.TgZ(0,"a"),e._uU(1),e.qZA()),2&c){const m=n.$implicit;e.xp6(1),e.Oqu(m)}}let s=(()=>{var c;class n{constructor(r){this.router=r,this.buildPath()}ngOnInit(){}buildPath(){const C=decodeURIComponent(this.breadscrumPath?this.breadscrumPath:this.router.url).split("/").map(a=>a.trim()).filter(a=>a.length>0).map(a=>a[0].toUpperCase()+a.slice(1,a.length)).map(a=>a.length>70?a.slice(0,70)+"...":a);this.pathList=[...C]}ngOnChanges(r){r.breadscrumPath&&!r.breadscrumPath.firstChange&&this.buildPath()}}return(c=n).\u0275fac=function(r){return new(r||c)(e.Y36(d.F0))},c.\u0275cmp=e.Xpm({type:c,selectors:[["breadscrumb"]],inputs:{breadscrumPath:"breadscrumPath"},features:[e.TTD],decls:2,vars:1,consts:[[1,"breadcrumb","flat"],[4,"ngFor","ngForOf"]],template:function(r,C){1&r&&(e.TgZ(0,"div",0),e.YNc(1,g,2,1,"a",1),e.qZA()),2&r&&(e.xp6(1),e.Q6J("ngForOf",C.pathList))},dependencies:[p.sg],styles:['.breadcrumb[_ngcontent-%COMP%]{display:inline-block;counter-reset:flag;padding:10px 0;min-width:-moz-fit-content;min-width:fit-content;z-index:9;-webkit-user-select:none;user-select:none;background:white!important}.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]{text-decoration:none;outline:none;display:block;float:left;font-size:14px;line-height:22px;color:#fff;padding:0 10px 0 20px;background:#333;background:linear-gradient(#333,#111);position:relative;font-weight:600}.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:first-child{border-radius:5px 0 0 5px}.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:last-child{border-radius:0 5px 5px 0}.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:last-child:after{content:none}.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:hover{background:#111;background:linear-gradient(#333,#111)}.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:hover:after{background:#222;background:linear-gradient(145deg,#333,#222)}.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:after{content:"";position:absolute;top:1px;right:-9px;width:20px;height:20px;transform:scale(.707) rotate(45deg);z-index:1;background:#555;background:linear-gradient(135deg,#777,#333);box-shadow:1px -1px 0 1px var(--main-color),2px -2px 0 1px var(--main-color);border-radius:0 5px 0 50px}.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:before{counter-increment:flag;border-radius:100%;height:15px;line-height:15px;margin:4px 0;position:absolute;top:2px;background:white!important;font-weight:700;text-align:center;font-size:10px}.breadcrumb[_ngcontent-%COMP%]   a.active[_ngcontent-%COMP%]{background:#111;color:#eee;background:linear-gradient(#333,#111)}.breadcrumb[_ngcontent-%COMP%]   a.active[_ngcontent-%COMP%]:after{background:#222;background:linear-gradient(145deg,#333,#222)}.flat[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]{background:white!important;color:var(--main-color);transition:all .7s;font-weight:600}.flat[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:after{background:white!important;color:#eee;transition:all .7s}.flat[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:before{background:#111;box-shadow:0 0 0 1px var(--main-color)}.flat[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:hover{background:var(--main-color)!important;color:#fff!important}.flat[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:hover:after{background:var(--main-color)!important}.flat[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:hover:before{color:#111!important}.flat[_ngcontent-%COMP%]   a.active[_ngcontent-%COMP%]{background:var(--main-color)!important;color:#fff!important}.flat[_ngcontent-%COMP%]   a.active[_ngcontent-%COMP%]:after{background:var(--main-color)!important}.flat[_ngcontent-%COMP%]   a.active[_ngcontent-%COMP%]:before{color:#222!important}@media screen and (max-width: 570px){.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]{padding-left:20px}.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:before{display:none}.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:last-child{padding-left:20px}.breadcrumb[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:first-child{padding-left:10px}}']}),n})()},8802:(E,l,t)=>{t.d(l,{x:()=>p});var e=t(6814),d=t(6689);let p=(()=>{var g;class s{}return(g=s).\u0275fac=function(n){return new(n||g)},g.\u0275mod=d.oAB({type:g}),g.\u0275inj=d.cJS({imports:[e.ez]}),s})()},7639:(E,l,t)=>{t.d(l,{G:()=>e});class e{constructor(){this.type=null}}}}]);