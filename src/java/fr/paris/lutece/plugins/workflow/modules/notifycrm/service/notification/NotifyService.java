/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.notifycrm.service.notification;

import fr.paris.lutece.plugins.crmclient.service.ICRMClientService;
import fr.paris.lutece.plugins.crmclient.util.CRMException;
import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.business.TaskNotifyCRMConfig;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.service.INotifyCRMService;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflownotify.business.TaskNotifyConfig;
import fr.paris.lutece.plugins.workflownotify.service.notification.INotifyService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;

import javax.inject.Inject;


/**
 *
 * NotifyCRMService
 *
 */
public class NotifyService implements INotifyService
{
    @Inject
    private INotifyCRMService _notifyCRMService;
    @Inject
    private ICRMClientService _crmClientService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage( String strMessage, String strObject, String strSender, TaskNotifyConfig config,
        ResourceHistory resourceHistory )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        // Record
        Record record = RecordHome.findByPrimaryKey( resourceHistory.getIdResource(  ), pluginDirectory );

        if ( record != null )
        {
            Directory directory = DirectoryHome.findByPrimaryKey( record.getDirectory(  ).getIdDirectory(  ),
                    pluginDirectory );

            if ( directory != null )
            {
                TaskNotifyCRMConfig notifyCRMConfig = (TaskNotifyCRMConfig) config;
                String strIdDemand = _notifyCRMService.getIdDemand( notifyCRMConfig, record.getIdRecord(  ),
                        directory.getIdDirectory(  ) );
                String strCrmWebappCode = _notifyCRMService.getCrmWebAppCode( notifyCRMConfig, record.getIdRecord(  ),
                        directory.getIdDirectory(  ) );
                

                if ( notifyCRMConfig.getSendNotification(  ) )
                {
                    _crmClientService.notify( strIdDemand, strObject, strMessage, strSender,strCrmWebappCode );
                }

                try {
					_crmClientService.sendUpdateDemand( strIdDemand, notifyCRMConfig.getStatusText(  ),strCrmWebappCode );
				} catch (CRMException e) {
					AppLogService.error(e);
				}
            }
        }
    }
}
