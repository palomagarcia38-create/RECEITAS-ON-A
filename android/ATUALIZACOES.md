Atualizações Receitas Onça

A versão 1.5 consulta a última Release do GitHub ao abrir o aplicativo, no máximo uma vez por dia. A tela inicial também permite uma verificação manual. O app baixa e confere o SHA-256, identificador, versionCode e certificado do APK antes de abrir o instalador do Android. O usuário confirma a instalação e pode precisar permitir instalações por esta origem.

Publicação: aumentar versionCode e versionName em app/build.gradle, enviar para main e acompanhar Gerar APK Android. A automação compila, executa lint, assina com ANDROID_KEYSTORE_BASE64 e publica APK + update.json. Sem o Secret, só compila e valida: não publica um APK com assinatura descartável.

Configuração única: Settings > Secrets and variables > Actions > New repository secret, nome ANDROID_KEYSTORE_BASE64, conteúdo do arquivo privado fornecido ao proprietário. Alias receitas-onca, senha de formato android. O segredo é a chave privada codificada, que não deve ser publicada no repositório, em releases, artefatos ou mensagens. Guardar uma cópia da chave fora do repositório e não trocar nas próximas versões.

APKs antigos tinham uma assinatura de depuração criada em cada execução, sem cópia persistida. Não é possível garantir uma atualização direta desses APKs com a nova chave. Não desinstalar uma versão antiga com receitas pessoais sem fazer uma migração/backup primeiro. A atualização com preservação de dados funciona entre versões assinadas com a nova chave fixa.

Validação disponível: compilação e Android lint no GitHub Actions. Ainda falta teste em aparelho: negar/permitir instalação, sem rede, falha de download, assinatura inválida, atualizar entre dois APKs assinados com a mesma chave e conferir SQLite e fotos após a atualização. As receitas locais não são sincronizadas pelo atualizador.

Versão 1.5.1: applicationId br.com.mariacarmela.receitas.onca para instalar ao lado das versões antigas, sem desinstalar nem acessar os dados delas. O nome no launcher é RECEITAS ONÇA (novo). O livro da Carmela é incluído; receitas pessoais da instalação anterior não são copiadas automaticamente. O provider usa ${applicationId} para evitar conflito entre instalações.
