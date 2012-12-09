addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.8.4")

resolvers += Classpaths.typesafeResolver

resolvers += "scct-github-repository" at "http://mtkopone.github.com/scct/maven-repo"

addSbtPlugin("reaktor" % "sbt-scct" % "0.2-SNAPSHOT")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.2.0")

resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
