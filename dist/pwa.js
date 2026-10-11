let installPrompt;
window.addEventListener('beforeinstallprompt',e=>{
 e.preventDefault();installPrompt=e;
 if(document.querySelector('#install-app'))return;
 const b=document.createElement('button');b.id='install-app';b.className='quiet';b.textContent='↓ Instalar app';
 document.querySelector('.storage').append(b);
 b.onclick=async()=>{await installPrompt.prompt();const result=await installPrompt.userChoice;if(result.outcome==='accepted')b.remove();installPrompt=null};
});
window.addEventListener('appinstalled',()=>document.querySelector('#install-app')?.remove());
if('serviceWorker' in navigator&&location.protocol==='https:'){
 navigator.serviceWorker.register('./sw.js').then(reg=>{
  function offer(){if(!reg.waiting||document.querySelector('#update-app'))return;
   const b=document.createElement('button');b.id='update-app';b.className='quiet';b.textContent='↻ Atualização disponível';document.querySelector('.storage').append(b);
   b.onclick=()=>{if(document.querySelector('dialog[open]')){toast('Guarde sua receita e feche a tela antes de atualizar.');return;}reg.waiting.postMessage('ACTIVATE_UPDATE')};
  }
  offer();reg.addEventListener('updatefound',()=>{const worker=reg.installing;worker?.addEventListener('statechange',()=>{if(worker.state==='installed'&&navigator.serviceWorker.controller)offer()})});
  let refreshing=false;navigator.serviceWorker.addEventListener('controllerchange',()=>{if(refreshing||!navigator.serviceWorker.controller)return;refreshing=true;if(reg.waiting===null&&!document.querySelector('#update-app'))return;location.reload()});
 }).catch(()=>{});
}
