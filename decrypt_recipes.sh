#!/bin/sh

THISDIR="$(dirname $(readlink -f $0))"

read -p "Enter the password: " PASSWORD

# --batch to prevent interactive command --yes to assume "yes" for questions
gpg --quiet --batch --yes --decrypt --passphrase="$PASSWORD" \
--output $THISDIR/src/main/java/com/airesnor/wuxiacraft/profession/alchemy/Recipes.java \
$THISDIR/src/main/java/com/airesnor/wuxiacraft/profession/alchemy/Recipes.Encrypted