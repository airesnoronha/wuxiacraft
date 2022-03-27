THISDIR="$(dirname $(readlink -f $0))"

read -p "block model name? " MODEL_NAME

echo "{
	\"variants\": {
    \"\":{
      \"model\":\"wuxiacraft:block/$MODEL_NAME\"
    }
  }
}" >> $THISDIR/src/main/resources/assets/wuxiacraft/blockstates/$MODEL_NAME.json

git add $THISDIR/src/main/resources/assets/wuxiacraft/blockstates/$MODEL_NAME.json