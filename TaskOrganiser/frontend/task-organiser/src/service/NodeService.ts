import { ApiService } from './ApiService';

export interface TreeNode {
    key: string;
    label: string;
    data: string;
    icon: string;
    children?: TreeNode[];
}

class NodeService {
    private apiService: ApiService;

    constructor() {
        this.apiService = new ApiService();
    }

    async getTreeNodes(): Promise<TreeNode[]> {
        const response = await this.apiService.get<TreeNode[]>('/tree-nodes');
        return response.data;
    }

    async getTreeTableNodes(): Promise<TreeNode[]> {
        const response = await this.apiService.get<TreeNode[]>('/tree-table-nodes');
        return response.data;
    }
}

export default new NodeService();