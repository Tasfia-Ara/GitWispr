// graph.js - Interactive graph visualization

function initializeGraph() {
    console.log('Initializing graph...');

    // Create or get container
    let container = document.getElementById('graph-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'graph-container';
        container.style.position = 'fixed';
        container.style.left = '16px';
        container.style.top = '150px';
        container.style.width = 'calc(100% - 250px)';
        container.style.height = '500px';
        container.style.border = '1px solid #ddd';
        container.style.borderRadius = '8px';
        container.style.backgroundColor = '#fafafa';
        container.style.zIndex = '1000';
        document.body.appendChild(container);
    }

    // Load vis.js if not already loaded
    if (typeof vis === 'undefined') {
        const script = document.createElement('script');
        script.src = 'https://unpkg.com/vis-network@9.1.2/dist/vis-network.min.js';
        script.onload = function() {
            console.log('vis.js loaded');
            setTimeout(createGraph, 100);
        };
        document.head.appendChild(script);
    } else {
        createGraph();
    }
}

function createGraph() {
    console.log('Creating graph...');
    const container = document.getElementById('graph-container');

    // Initial data - only show root node
    const nodesData = [
        {
            id: 'app',
            label: 'App.kt',
            color: '#F44336',
            shape: 'circle',
            font: { size: 14, color: '#000' },
            size: 25
        }
    ];

    const edgesData = [];

    const nodes = new vis.DataSet(nodesData);
    const edges = new vis.DataSet(edgesData);

    const data = { nodes: nodes, edges: edges };

    const options = {
        physics: {
            enabled: true,
            stabilization: {
                iterations: 200
            },
            barnesHut: {
                gravitationalConstant: -2000,
                springConstant: 0.001,
                springLength: 200,
                avoidOverlap: 0.5
            }
        },
        interaction: {
            hover: true,
            tooltipDelay: 200,
            navigationButtons: true,
            keyboard: true
        },
        nodes: {
            shape: 'circle',
            font: {
                size: 14,
                color: '#000'
            },
            borderWidth: 2,
            shadow: true,
            size: 25
        },
        edges: {
            width: 2,
            arrows: 'to',
            smooth: {
                type: 'continuous'
            },
            color: {
                color: '#848484',
                highlight: '#000000'
            }
        }
    };

    window.network = new vis.Network(container, data, options);

    // Store expanded nodes
    window.expandedNodes = new Set(['app']);

    // Store all node data
    window.allNodesData = {
        'app': { label: 'App.kt', description: 'Application entry point and composition root', color: '#F44336', children: ['authController', 'repoController'] },
        'authController': { label: 'AuthController', description: 'Handles authentication flow coordination', color: '#F44336', children: ['loginUseCase', 'authPresenter'] },
        'repoController': { label: 'RepoController', description: 'Manages repository-related operations', color: '#F44336', children: ['fetchRepoUseCase', 'repoPresenter'] },
        'loginUseCase': { label: 'LoginUseCase', description: 'Business logic for user authentication', color: '#2196F3', children: ['authGateway', 'userEntity'] },
        'fetchRepoUseCase': { label: 'FetchRepoUseCase', description: 'Retrieves repository information', color: '#2196F3', children: ['githubGateway', 'repoEntity'] },
        'authGateway': { label: 'AuthGateway', description: 'Interface for authentication data access', color: '#FF9800', children: ['authService'] },
        'githubGateway': { label: 'GitHubGateway', description: 'Interface for GitHub API access', color: '#FF9800', children: ['githubService'] },
        'authService': { label: 'AuthService', description: 'OAuth implementation with GitHub', color: '#607D8B', children: [] },
        'githubService': { label: 'GitHubService', description: 'HTTP client for GitHub API', color: '#607D8B', children: [] },
        'authPresenter': { label: 'AuthPresenter', description: 'Formats authentication data for UI', color: '#9C27B0', children: ['authModel'] },
        'repoPresenter': { label: 'RepoPresenter', description: 'Formats repository data for display', color: '#9C27B0', children: ['repoModel'] },
        'userEntity': { label: 'User', description: 'Core user business object', color: '#4CAF50', children: [] },
        'repoEntity': { label: 'Repository', description: 'Core repository business object', color: '#4CAF50', children: [] },
        'authModel': { label: 'AuthViewModel', description: 'UI state for authentication screen', color: '#00BCD4', children: [] },
        'repoModel': { label: 'RepoViewModel', description: 'UI state for repository list', color: '#00BCD4', children: [] }
    };

    // Single click handler - expand nodes
    window.network.on('click', function(params) {
        if (params.nodes.length > 0) {
            const nodeId = params.nodes[0];
            expandNode(nodeId);
        }
    });

    // Double-click handler - show description
    window.network.on('doubleClick', function(params) {
        if (params.nodes.length > 0) {
            const nodeId = params.nodes[0];
            const nodeData = window.allNodesData[nodeId];
            if (nodeData) {
                alert(nodeData.label + '\n\n' + nodeData.description);
            }
        }
    });

    console.log('Graph created successfully');
}

function expandNode(nodeId) {
    if (!window.expandedNodes.has(nodeId)) {
        window.expandedNodes.add(nodeId);

        const nodeData = window.allNodesData[nodeId];
        if (nodeData && nodeData.children && nodeData.children.length > 0) {
            nodeData.children.forEach(function(childId) {
                const childData = window.allNodesData[childId];
                if (childData && !window.network.body.data.nodes.get(childId)) {
                    window.network.body.data.nodes.add({
                        id: childId,
                        label: childData.label,
                        color: childData.color,
                        shape: 'circle',
                        font: { size: 14, color: '#000' },
                        size: 25
                    });

                    window.network.body.data.edges.add({
                        from: nodeId,
                        to: childId
                    });
                }
            });
        }
    }
}

function resetGraphToDefault() {
    console.log('Resetting graph...');
    if (window.network) {
        window.expandedNodes = new Set(['app']);

        const nodesData = [
            {
                id: 'app',
                label: 'App.kt',
                color: '#F44336',
                shape: 'circle',
                font: { size: 14, color: '#000' },
                size: 25
            }
        ];

        window.network.setData({
            nodes: new vis.DataSet(nodesData),
            edges: new vis.DataSet([])
        });

        console.log('Graph reset complete');
    }
}