# RECEITAS ONÇA

Livro de receitas pessoal inspirado na Maria Carmela. Aplicativo web instalável no Android, sem Supabase e sem dependências de compilação.

## Publicação no GitHub Pages

O repositório deve conter esta pasta na raiz. Em **Settings → Pages → Source**, escolha **GitHub Actions**. A cada atualização na branch `main`, o workflow publica o aplicativo. O endereço final é informado pelo GitHub na execução do workflow e na tela Pages.

Abra esse endereço no Chrome do Android e use **Instalar aplicativo** no menu, ou o botão **Instalar app** quando disponível. A instalação depende de HTTPS e suporte do navegador; abrir `index.html` como arquivo não instala o app.

## Atualizações e privacidade

O workflow gera uma versão de cache a partir do conteúdo. Novas versões oferecem o botão **Atualização disponível**, sem precisar reinstalar. Feche o formulário de receita antes de atualizar. Os dados do livro permanecem no IndexedDB, separado do cache do aplicativo.

O site e o código são públicos no GitHub Pages gratuito. Receitas, fotos e vídeos cadastrados são armazenados somente no navegador do usuário: não são enviados ao GitHub. Não há autenticação ou sincronização entre aparelhos. Use **Exportar livro** para guardar uma cópia completa e **Importar livro** no outro aparelho. Limpar o armazenamento do navegador pode apagar o acervo.

A imagem da abertura é ilustrativa e foi gerada. Nenhuma receita da família foi inventada.

## Verificação

`node scripts/prepare-pages.mjs` valida a sintaxe JavaScript, os ícones e o manifesto e prepara o cache. A instalação no Android e a atualização entre versões ainda precisam ser verificadas em aparelho real.
