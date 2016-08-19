def url     = 'https://api.github.com/repos/loadimpact/loadimpact-sdk-java/commits' 
def slurper = new groovy.json.JsonSlurper()
def fmt     = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
def commits = slurper.parse(new URL(url))
    .collect {
        [
            date: fmt.parse(it.commit.author.date), 
            name: it.commit.author.name, 
            msg: it.commit.message.replaceAll(/[\n]+/,' ').replaceAll(/[*]/,'')
        ]
    }
    .collect {
        String.format('// %tF (%s) %s', it.date, it.name, it.msg)
    }
    .join('\n')
println commits
