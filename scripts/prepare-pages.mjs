import {readFile, writeFile, readdir, stat} from 'node:fs/promises';
import {createHash} from 'node:crypto';
import {Script} from 'node:vm';
const hash=createHash('sha256');
async function scan(dir){for(const name of (await readdir(dir)).sort()){
 const path=`${dir}/${name}`;
 if((await stat(path)).isDirectory())await scan(path);
 else if(name!=='sw.js'){hash.update(path);hash.update(await readFile(path));}
}}
for(const name of ['app.js','pwa.js','sw.js'])new Script(await readFile(`dist/${name}`,'utf8'),{filename:name});
const manifest=JSON.parse(await readFile('dist/manifest.webmanifest','utf8'));
if(!manifest.name||manifest.display!=='standalone')throw new Error('Manifesto incompleto');
for(const icon of manifest.icons)await stat(`dist/${icon.src}`);
await scan('dist');
const version=hash.digest('hex').slice(0,16);
const worker=await readFile('dist/sw.js','utf8');
await writeFile('dist/sw.js',worker.replace(/const CACHE='[^']+';/,`const CACHE='maria-carmela-shell-${version}';`));
console.log(`Versão preparada: ${version}`);
