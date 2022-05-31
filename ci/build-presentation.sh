#!/bin/bash
cd "$(dirname "$0")" || exit
cd .. || exit
docker run --rm -v $PWD:/documents curs/asciidoctor-od rm target -Rf
docker run --rm -v $PWD:/documents curs/asciidoctor-od mkdir target
docker run --rm -v $PWD:/documents curs/asciidoctor-od cp demo demo-bills doc target/ -R
pushd target/doc/prezentacziya || exit
./_build.sh
popd

echo "Generated up-to-doc.html and up-to-doc.pdf here: $PWD/target/doc/prezentacziya"