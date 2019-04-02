
# these are the same for all
dpath=C:/Users/eeeki/OneDrive/Documents/maven-repos/
dgroup=michaelfrancenelson
dfile=$dprojectpath$dartifactid-$dversion.jar
echo $dfile

mvn install:install-file -DgroupId=$dgroup -DartifactId=$dartifactid -Dversion=$dversion -Dpackaging=jar -Dfile=$dfile -DlocalRepositoryPath=$dpath


git add michaelfrancenelson/

git commit -m "updating artifact `$dartifactid` version `$dversion`"

