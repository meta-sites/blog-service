"use strict";(self.webpackChunkit_blog=self.webpackChunkit_blog||[]).push([[631],{5631:(A,c,t)=>{t.r(c),t.d(c,{ReadBookModule:()=>S});var l=t(6814),m=t(5861),u=t(7639),n=t(6689),f=t(2911),p=t(9304),g=t(5593),h=t(6068),d=t(6014),v=t(8008),C=t(122),P=t(9836),M=t(3423);function B(o,a){if(1&o&&(n.TgZ(0,"div",6),n._uU(1),n.qZA()),2&o){const i=n.oxw();n.xp6(1),n.Oqu(i.pdf.name)}}let O=(()=>{var o;class a{constructor(r,e,s,F,I,T){this.userService=r,this.noticeFunction=e,this.pdfService=s,this.commonFunction=F,this.route=I,this.router=T,this.route.paramMap.subscribe(U=>{const Y=U.get("id")||void 0;this.getAllBookWithPaging(Y)})}ngOnInit(){this.userInfo=this.userService.getCurrentUserInfo()}buildFilterByName(r){const e=new u.G;return e.name=r,e.numsPerPage=1,e.pageNumber=0,e}getAllBookWithPaging(r){var e=this;return(0,m.Z)(function*(){const s=yield e.pdfService.filter(e.buildFilterByName(r));if(s.length&&(e.pdf=s[0]),!e.isSubcribe(e.pdf.id))return e.router.routeReuseStrategy.shouldReuseRoute=()=>!1,e.router.onSameUrlNavigation="reload",void e.router.navigate(["/tai-lieu"]);e.breadscrumPath=`T\xe0i li\u1ec7u/${e.pdf.name}`})()}isSubcribe(r){return this.userInfo.books?.find(e=>e==r)}}return(o=a).\u0275fac=function(r){return new(r||o)(n.Y36(f.K),n.Y36(p.l),n.Y36(g.F),n.Y36(h.o),n.Y36(d.gz),n.Y36(d.F0))},o.\u0275cmp=n.Xpm({type:o,selectors:[["app-read-book"]],decls:9,vars:3,consts:[[1,"wraper-container"],[3,"breadscrumPath"],[1,"wraper-main"],[1,"left"],["class","title",4,"ngIf"],[3,"cvInfo"],[1,"title"]],template:function(r,e){1&r&&(n.TgZ(0,"div"),n._UZ(1,"header"),n.TgZ(2,"div",0),n._UZ(3,"breadscrumb",1),n.TgZ(4,"div",2)(5,"div",3),n.YNc(6,B,2,1,"div",4),n._UZ(7,"pdf-render",5),n.qZA(),n._UZ(8,"short-info-blog"),n.qZA()()()),2&r&&(n.xp6(3),n.Q6J("breadscrumPath",e.breadscrumPath),n.xp6(3),n.Q6J("ngIf",e.pdf),n.xp6(1),n.Q6J("cvInfo",e.pdf))},dependencies:[l.O5,v.e,C.G,P.q,M.a],styles:[".wraper-container[_ngcontent-%COMP%]{padding:15px 60px}.wraper-container[_ngcontent-%COMP%]   .wraper-main[_ngcontent-%COMP%]{display:grid;grid-template-columns:3.5fr 1fr;grid-gap:40px}.wraper-container[_ngcontent-%COMP%]   .wraper-main[_ngcontent-%COMP%]   .left[_ngcontent-%COMP%]   .title[_ngcontent-%COMP%]{color:var(--main-color);font-size:24px;font-style:normal;font-weight:700;line-height:normal;margin:0 0 20px;-webkit-user-select:none;user-select:none}@media screen and (max-width: 1000px){.wraper-container[_ngcontent-%COMP%]   .wraper-main[_ngcontent-%COMP%]{grid-template-columns:1fr!important}}@media screen and (min-width: 1000px){.wraper-container[_ngcontent-%COMP%]   .wraper-main[_ngcontent-%COMP%]{grid-template-columns:3.5fr 1fr!important}}"]}),a})();var x=t(8802),y=t(8567),R=t(8457),Z=t(4551);const b=[{path:"",component:O}];let S=(()=>{var o;class a{}return(o=a).\u0275fac=function(r){return new(r||o)},o.\u0275mod=n.oAB({type:o}),o.\u0275inj=n.cJS({imports:[l.ez,x.x,y.O,R.M,Z.$,d.Bz.forChild(b)]}),a})()}}]);