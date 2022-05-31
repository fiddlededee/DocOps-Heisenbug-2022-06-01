docker run --rm -v $PWD/../..:/documents/ -w /documents/doc/prezentacziya curs/asciidoctor-od  asciidoctor-revealjs -a revealjsdir=https://cdnjs.cloudflare.com/ajax/libs/reveal.js/3.9.2 -r asciidoctor-diagram up-to-doc.adoc
docker run --rm -v $PWD/../..:/documents/ -w /documents/doc/prezentacziya curs/asciidoctor-od chmod 777 .
docker run --rm -t -v $PWD/../..:/slides -w /slides/doc/prezentacziya astefanutti/decktape -s 1600x1580 --slides 1-500 reveal ./up-to-doc.html pre.pdf
docker run --rm -v $PWD:/app -w /app curs/asciidoctor-od ./pdfScale.sh -v -v -r 'custom mm 297 167' --yoffset -100  -f disable -s 1.7 pre.pdf up-to-doc
