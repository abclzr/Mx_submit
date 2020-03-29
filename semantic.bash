# this script is called when the judge wants our compiler to compile a source file.
# print the compiled source, i.e. asm code, directly to stdout.
# don't print anything other to stdout.
# if you would like to print some debug information, please go to stderr.
set -e
cd "$(dirname "$0")"
export CCHK="java -ea -classpath /ulib/java/antlr-4.8-complete.jar:/mnt/c/Users/Dedicatus545/Desktop/Mx_star_Compiler/lib/antlr-4.8-complete.jar:./bin Main"
cat > test.txt   # save everything in stdin to program.txt
$CCHK