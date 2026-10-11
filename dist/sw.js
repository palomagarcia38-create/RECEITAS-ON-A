const CACHE='maria-carmela-shell-fd9f0fc5282e661c';
const ROOT=new URL('./',self.location).href;
const FILES=['./','index.html','style.css','app.js','pwa.js','manifest.webmanifest','images/maria-carmela.webp','icons/maria-carmela-192.png','icons/maria-carmela-512.png'].map(p=>new URL(p,ROOT).href);
self.addEventListener('install',e=>e.waitUntil(caches.open(CACHE).then(c=>c.addAll(FILES))));
self.addEventListener('activate',e=>e.waitUntil(caches.keys().then(keys=>Promise.all(keys.filter(k=>k.startsWith('maria-carmela-shell-')&&k!==CACHE).map(k=>caches.delete(k)))).then(()=>self.clients.claim())));
self.addEventListener('message',e=>{if(e.data==='ACTIVATE_UPDATE')self.skipWaiting()});
self.addEventListener('fetch',e=>{
 const u=new URL(e.request.url);
 if(e.request.method!=='GET'||u.origin!==self.location.origin||!FILES.includes(u.href))return;
 e.respondWith(caches.match(e.request).then(cached=>cached||fetch(e.request)));
});
