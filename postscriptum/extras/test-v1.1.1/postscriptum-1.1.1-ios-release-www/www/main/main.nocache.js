function main(){var Y='',V=' top: -1000px;',tb='" for "gwt:onLoadErrorFn"',rb='" for "gwt:onPropertyErrorFn"',cb='");',ub='#',Gb='&',ic='.cache.js',wb='/',Cb='//',$b='9F5AEDFAB00B813119D7E885D36B4678',hc=':',_b=':1',ac=':2',bc=':3',dc=':4',ec=':5',fc=':6',gc=':7',lb='::',Ib=';',X='<!doctype html>',Z='<html><head><\/head><body><\/body><\/html>',ob='=',vb='?',qb='Bad handler "',W='CSS1Compat',ab='Chrome',_='DOMContentLoaded',Q='DUMMY',Lb='Unexpected exception in locale detection, using default: ',Kb='_',Jb='__gwt_Locale',Qb='android',Bb='base',zb='baseUrl',L='begin',Ub='blackberry',R='body',K='bootstrap',yb='clear.cache.gif',nb='content',Eb='default',pc='end',bb='eval("',Vb='file://',M='gwt.codesvr.main=',N='gwt.codesvr=',sb='gwt:onLoadErrorFn',pb='gwt:onPropertyErrorFn',mb='gwt:property',hb='head',mc='href',S='iframe',xb='img',Rb='ipad',Tb='iphone',Sb='ipod',cc='it',eb='javascript',T='javascript:""',jc='link',nc='loadExternalRefs',Db='locale',Fb='locale=',O='main',Zb='main.devmode.js',Ab='main.nocache.js',kb='main::',ib='meta',Hb='mgwtLanguage=',Mb='mobile.user.agent',Nb='mobilesafari',gb='moduleRequested',fb='moduleStartup',jb='name',Xb='no',Ob='not_mobile',Pb='phonegap.env',U='position:absolute; width:0; height:0; border:none; left: -1000px;',kc='rel',oc='roundedpanel.css',db='script',Yb='selectingPermutation',P='startup',lc='stylesheet',$='undefined',Wb='yes';var s=window;var t=document;v(K,L);function u(){var a=s.location.search;return a.indexOf(M)!=-1||a.indexOf(N)!=-1}
function v(a,b){if(s.__gwtStatsEvent){s.__gwtStatsEvent({moduleName:O,sessionId:s.__gwtStatsSessionId,subSystem:P,evtGroup:a,millis:(new Date).getTime(),type:b})}}
main.__sendStats=v;main.__moduleName=O;main.__errFn=null;main.__moduleBase=Q;main.__softPermutationId=0;main.__computePropValue=null;main.__getPropMap=null;main.__gwtInstallCode=function(){};main.__gwtStartLoadingFragment=function(){return null};var w=function(){return false};var x=function(){return null};__propertyErrorFunction=null;var y=s.__gwt_activeModules=s.__gwt_activeModules||{};y[O]={moduleName:O};var z;function A(){C();return z}
function B(){C();return z.getElementsByTagName(R)[0]}
function C(){if(z){return}var a=t.createElement(S);a.src=T;a.id=O;a.style.cssText=U+V;a.tabIndex=-1;t.body.appendChild(a);z=a.contentDocument;if(!z){z=a.contentWindow.document}z.open();var b=document.compatMode==W?X:Y;z.write(b+Z);z.close()}
function D(k){function l(a){function b(){if(typeof t.readyState==$){return typeof t.body!=$&&t.body!=null}return /loaded|complete/.test(t.readyState)}
var c=b();if(c){a();return}function d(){if(!c){c=true;a();if(t.removeEventListener){t.removeEventListener(_,d,false)}if(e){clearInterval(e)}}}
if(t.addEventListener){t.addEventListener(_,d,false)}var e=setInterval(function(){if(b()){d()}},50)}
function m(c){function d(a,b){a.removeChild(b)}
var e=B();var f=A();var g;if(navigator.userAgent.indexOf(ab)>-1&&window.JSON){var h=f.createDocumentFragment();h.appendChild(f.createTextNode(bb));for(var i=0;i<c.length;i++){var j=window.JSON.stringify(c[i]);h.appendChild(f.createTextNode(j.substring(1,j.length-1)))}h.appendChild(f.createTextNode(cb));g=f.createElement(db);g.language=eb;g.appendChild(h);e.appendChild(g);d(e,g)}else{for(var i=0;i<c.length;i++){g=f.createElement(db);g.language=eb;g.text=c[i];e.appendChild(g);d(e,g)}}}
main.onScriptDownloaded=function(a){l(function(){m(a)})};v(fb,gb);var n=t.createElement(db);n.src=k;t.getElementsByTagName(hb)[0].appendChild(n)}
main.__startLoadingFragment=function(a){return G(a)};main.__installRunAsyncCode=function(a){var b=B();var c=A().createElement(db);c.language=eb;c.text=a;b.appendChild(c);b.removeChild(c)};function E(){var c={};var d;var e;var f=t.getElementsByTagName(ib);for(var g=0,h=f.length;g<h;++g){var i=f[g],j=i.getAttribute(jb),k;if(j){j=j.replace(kb,Y);if(j.indexOf(lb)>=0){continue}if(j==mb){k=i.getAttribute(nb);if(k){var l,m=k.indexOf(ob);if(m>=0){j=k.substring(0,m);l=k.substring(m+1)}else{j=k;l=Y}c[j]=l}}else if(j==pb){k=i.getAttribute(nb);if(k){try{d=eval(k)}catch(a){alert(qb+k+rb)}}}else if(j==sb){k=i.getAttribute(nb);if(k){try{e=eval(k)}catch(a){alert(qb+k+tb)}}}}}x=function(a){var b=c[a];return b==null?null:b};__propertyErrorFunction=d;main.__errFn=e}
function F(){function e(a){var b=a.lastIndexOf(ub);if(b==-1){b=a.length}var c=a.indexOf(vb);if(c==-1){c=a.length}var d=a.lastIndexOf(wb,Math.min(c,b));return d>=0?a.substring(0,d+1):Y}
function f(a){if(a.match(/^\w+:\/\//)){}else{var b=t.createElement(xb);b.src=a+yb;a=e(b.src)}return a}
function g(){var a=x(zb);if(a!=null){return a}return Y}
function h(){var a=t.getElementsByTagName(db);for(var b=0;b<a.length;++b){if(a[b].src.indexOf(Ab)!=-1){return e(a[b].src)}}return Y}
function i(){var a=t.getElementsByTagName(Bb);if(a.length>0){return a[a.length-1].href}return Y}
function j(){var a=t.location;return a.href==a.protocol+Cb+a.host+a.pathname+a.search+a.hash}
var k=g();if(k==Y){k=h()}if(k==Y){k=i()}if(k==Y&&j()){k=e(t.location.href)}k=f(k);return k}
function G(a){if(a.match(/^\//)){return a}if(a.match(/^[a-zA-Z]+:\/\//)){return a}return main.__moduleBase+a}
function H(){var k=[];var l;function m(a,b){var c=k;for(var d=0,e=a.length-1;d<e;++d){c=c[a[d]]||(c[a[d]]=[])}c[a[e]]=b}
var n=[];var o=[];function p(a){var b=o[a](),c=n[a];if(b in c){return b}var d=[];for(var e in c){d[c[e]]=e}if(__propertyErrorFunc){__propertyErrorFunc(a,d,b)}throw null}
o[Db]=function(){var b=null;var c=Eb;try{if(!b){var d=location.search;var e=d.indexOf(Fb);if(e>=0){var f=d.substring(e+7);var g=d.indexOf(Gb,e);if(g<0){g=d.length}b=d.substring(e+7,g)}}if(!b){var h=t.cookie;var i=h.indexOf(Hb);if(i>=0){var g=h.indexOf(Ib,i);if(g<0){g=h.length}b=h.substring(i+13,g)}}if(!b){b=x(Db)}if(!b){b=s[Jb]}if(b){c=b}while(b&&!w(Db,b)){var j=b.lastIndexOf(Kb);if(j<0){b=null;break}b=b.substring(0,j)}}catch(a){alert(Lb+a)}s[Jb]=c;return b||Eb};n[Db]={'default':0,it:1};o[Mb]=function(){return /(android|iphone|ipod|ipad)/i.test(window.navigator.userAgent)?Nb:Ob};n[Mb]={mobilesafari:0,not_mobile:1};o[Pb]=function(){{var a=window.navigator.userAgent.toLowerCase();if(a.indexOf(Qb)!=-1||(a.indexOf(Rb)!=-1||(a.indexOf(Sb)!=-1||(a.indexOf(Tb)!=-1||a.indexOf(Ub)!=-1)))){var b=document.location.href;if(b.indexOf(Vb)===0){return Wb}}return Xb}};n[Pb]={no:0,yes:1};w=function(a,b){return b in n[a]};main.__getPropMap=function(){var a={};for(var b in n){if(n.hasOwnProperty(b)){a[b]=p(b)}}return a};main.__computePropValue=p;s.__gwt_activeModules[O].bindings=main.__getPropMap;v(K,Yb);if(u()){return G(Zb)}var q;try{m([Eb,Nb,Xb],$b);m([Eb,Nb,Wb],$b+_b);m([Eb,Ob,Xb],$b+ac);m([Eb,Ob,Wb],$b+bc);m([cc,Nb,Xb],$b+dc);m([cc,Nb,Wb],$b+ec);m([cc,Ob,Xb],$b+fc);m([cc,Ob,Wb],$b+gc);q=k[p(Db)][p(Mb)][p(Pb)];var r=q.indexOf(hc);if(r!=-1){l=parseInt(q.substring(r+1),10);q=q.substring(0,r)}}catch(a){}main.__softPermutationId=l;return G(q+ic)}
function I(){if(!s.__gwt_stylesLoaded){s.__gwt_stylesLoaded={}}function c(a){if(!__gwt_stylesLoaded[a]){var b=t.createElement(jc);b.setAttribute(kc,lc);b.setAttribute(mc,G(a));t.getElementsByTagName(hb)[0].appendChild(b);__gwt_stylesLoaded[a]=true}}
v(nc,L);c(oc);v(nc,pc)}
E();main.__moduleBase=F();y[O].moduleBase=main.__moduleBase;var J=H();I();v(K,pc);D(J);return true}
main.succeeded=main();