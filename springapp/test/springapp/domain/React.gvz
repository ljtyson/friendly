digraph React {
    rankdir=LR
    node [shape=box style=filled fillcolor=lightgray]

    // Cluster: React Ecosystem
    subgraph cluster_react {
        label = "React Ecosystem"
        style = dashed
        color = gray

        react [label="React"]
        nextjs [label="Next.js"]
        router [label="React Router"]
        native [label="React Native"]
    }

    // Cluster: Deployment & Platforms
    subgraph cluster_platforms {
        label = "Platforms & Tools"
        style = dashed
        color = gray

        vercel [label="Vercel"]
        shopify [label="Shopify"]
        expo [label="Expo"]
    }

    // React Relationships
    react -> nextjs [label="Web Framework"]
    react -> router [label="Routing"]
    react -> native [label="Mobile"]

    // Tooling / Deployment
    nextjs -> vercel [label="Deploys to"]
    router -> shopify [label="Used in apps for"]
    native -> expo [label="Built with"]
}
