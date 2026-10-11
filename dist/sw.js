const CACHE='maria-carmela-shell-2450081d9ebe9030';
const ROOT=new URL('./',self.location).href;
const FILES=['./','index.html','style.css?v=onca-4','app.js?v=onca-4','pwa.js?v=onca-4','manifest.webmanifest','images/maria-carmela.webp','icons/maria-carmela-192.png','icons/maria-carmela-512.png'].map(p=>new URL(p,ROOT).href);
self.addEventListener('install',e=>e.waitUntil(caches.open(CACHE).then(c=>c.addAll(FILES))));
self.addEventListener('activate',e=>e.waitUntil(caches.keys().then(keys=>Promise.all(keys.filter(k=>k.startsWith('maria-carmela-shell-')&&k!==CACHE).map(k=>caches.delete(k)))).then(()=>self.clients.claim())));
self.addEventListener('message',e=>{if(e.data==='ACTIVATE_UPDATE')self.skipWaiting()});
self.addEventListener('fetch',e=>{
 const u=new URL(e.request.url);
 if(e.request.method!=='GET'||u.origin!==self.location.origin||!FILES.includes(u.href))return;
 e.respondWith(caches.match(e.request).then(cached=>cached||fetch(e.request)));
});
