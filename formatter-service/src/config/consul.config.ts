// src/config/consul.config.ts
import Consul from 'consul';
import * as os from 'os';

export class ConsulService {
  private consul: any;
  private serviceId: string;
  private serviceHost: string;

  constructor() {
    this.consul = new Consul({
      host: process.env.CONSUL_HOST || 'localhost',
      port: Number(process.env.CONSUL_PORT) || 8500,
    });
    this.serviceId = `formateur-service-${process.env.SERVICE_PORT || 3002}`;
    
    // Get the local machine's IP (same logic as Spring Boot's prefer-ip-address)
    this.serviceHost = this.getLocalIpAddress();
  }

  /**
   * Get local IP address - mimics Spring Boot's prefer-ip-address=true
   * This returns the actual network IP, not 127.0.0.1
   */
  private getLocalIpAddress(): string {
    const networkInterfaces = os.networkInterfaces();
    
    // Priority order for interface selection
    const preferredInterfaces = ['eth0', 'en0', 'Wi-Fi', 'Ethernet'];
    
    // First, try preferred interfaces
    for (const interfaceName of preferredInterfaces) {
      const iface = networkInterfaces[interfaceName];
      if (iface) {
        for (const alias of iface) {
          if (alias.family === 'IPv4' && !alias.internal) {
            console.log(` Using IP from ${interfaceName}: ${alias.address}`);
            return alias.address;
          }
        }
      }
    }
    
    // If no preferred interface found, scan all interfaces
    for (const interfaceName of Object.keys(networkInterfaces)) {
      const interfaces = networkInterfaces[interfaceName];
      if (!interfaces) continue;
      
      for (const iface of interfaces) {
        // Skip loopback and non-IPv4
        if (iface.family === 'IPv4' && !iface.internal) {
          console.log(` Using IP from ${interfaceName}: ${iface.address}`);
          return iface.address;
        }
      }
    }

    // Should never reach here, but fallback to localhost
    console.warn('  Could not find network IP, using 127.0.0.1 (may not work with Docker Consul)');
    return '127.0.0.1';
  }

  async registerService() {
    const port = parseInt(process.env.SERVICE_PORT || '3002');
    
    const serviceDefinition = {
      id: this.serviceId,
      name: 'formateur-service',
      address: this.serviceHost, // This will be the actual IP, not 127.0.0.1
      port: port,
      check: {
        http: `http://${this.serviceHost}:${port}/health`,
        interval: '10s',
        timeout: '5s',
        deregister_critical_service_after: '1m',
      },
      tags: ['formateur'],
      meta: {
        version: '1.0.0',
        environment: process.env.NODE_ENV || 'development',
      },
    };

    try {
      await this.consul.agent.service.register(serviceDefinition);
      console.log('╔══════════════════════════════════════════════════════╗');
      console.log('║   Service Registered Successfully with Consul        ║');
      console.log('╠══════════════════════════════════════════════════════╣');
      console.log(`║  Service ID: ${this.serviceId.padEnd(37)}            ║`);
      console.log(`║  Service Name: formateur-service${' '.repeat(21)}    ║`);
      console.log(`║  Address: ${this.serviceHost}:${port}${' '.repeat(37 - this.serviceHost.length - port.toString().length)}║`);
      console.log(`║  Health Check: http://${this.serviceHost}:${port}/health${' '.repeat(20 - this.serviceHost.length - port.toString().length)}║`);
      console.log('╚══════════════════════════════════════════════════════╝');
    } catch (error) {
      console.error(' Failed to register service with Consul:', error);
      throw error;
    }
  }

  async deregisterService() {
    try {
      await this.consul.agent.service.deregister(this.serviceId);
      console.log(` Service deregistered from Consul: ${this.serviceId}`);
    } catch (error) {
      console.error(' Failed to deregister service from Consul:', error);
    }
  }

  async getService(serviceName: string): Promise<any[]> {
    try {
      const result = await this.consul.health.service({
        service: serviceName,
        passing: true,
      });
      return result;
    } catch (error) {
      console.error(`Failed to get service ${serviceName} from Consul:`, error);
      throw error;
    }
  }

  async checkHealth(): Promise<boolean> {
    try {
      const services = await this.consul.health.service({
        service: 'formateur-service',
        passing: true,
      });
      return services.length > 0;
    } catch (error) {
      return false;
    }
  }
}